package com.subject.todo.service;

import com.subject.todo.code.Priority;
import com.subject.todo.code.SortBy;
import com.subject.todo.code.TodoStatus;
import com.subject.todo.service.model.SearchTodoDto;
import com.subject.todo.service.model.TodoDto;
import com.subject.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.subject.todo.service.model.TodoMapper;
import com.subject.todo.repository.Todo;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;
    private final Integer NEXT_PRIORITY_VALUE = 1;
    private final Integer DEFAULT_PRIORITY_VALUE = 0;

    @Autowired
    public TodoService(TodoRepository todoRepository, TodoMapper todoMapper) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
    }

    //@Transactional(readOnly = true)
    public List<TodoDto> searchAllTodos() {
        List<Todo> todos = todoRepository.findAll();
        return todoMapper.toDtoList(todos);
    }

    // 특정 담당자, 특정 일자의 list 를 중요도/우선순위로 정렬하여 조회할 수 있다.
    // @Transactional(readOnly = true)
    public List<TodoDto> searchTodos(SearchTodoDto searchTodoDto) {
        List<Todo> todos = todoRepository.searchTodos(searchTodoDto);
        return todoMapper.toDtoList(todos);
    }

    // 생성
    //@Transactional
    public TodoDto create(TodoDto todoDto)  {
        // medium 등급의 가장 마지막 우선순위
        Optional<Todo> optionalTodo = todoRepository.findTopByUserIdAndPriority(todoDto.getUserId(), SortBy.ASC.isAsc(), Priority.MEDIUM, todoDto.getPracticeDate());
        Todo todo = null;
        if (optionalTodo.isPresent()) {
            Todo mediumTodo = optionalTodo.get();
            todo = todoDto.createEntity(mediumTodo.getPriorityValue() + NEXT_PRIORITY_VALUE);
        } else { // 없으면 0으로 생성
            todo = todoDto.createEntity(DEFAULT_PRIORITY_VALUE);
        }
        return todoRepository.save(todo).toDto();
    }

    // 위임
    // 위임한 담당자에게 todo는 위임 + 위임받은 사람이 표시된다.
    //@Transactional
    public TodoDto delegate(long todoId, long delegatedUserId) {
        Todo prevTodo = getTodoDtoById(todoId);
        if (prevTodo.getIsDelegated()) {
            throw new RuntimeException("이미 위임된 태스크입니다.");
        }

        // 위임 할 태스크
        prevTodo.delegate(delegatedUserId);
        sortPriorityByReducing(prevTodo.toDto());
        todoRepository.save(prevTodo);

        // 위임받은 태스크
        TodoDto delegatedTodoDto = prevTodo.toDto(); // 위임할 태스크 복사
        setDelegatedTask(delegatedTodoDto);
        todoRepository.save(delegatedTodoDto.toEntity());

        return prevTodo.toDto();
    }

    // 위임받은 태스크
    private void setDelegatedTask(TodoDto todoDto) {
        todoDto.setPrevUserId(todoDto.getUserId());
        todoDto.setUserId(todoDto.getDelegatedUserId());
        todoDto.setStatus(TodoStatus.WAITING);
        todoDto.setDeleted(Boolean.FALSE);
        todoDto.setPrevTodoId(todoDto.getId());
        todoDto.setDelegated(Boolean.FALSE);
        todoDto.setDelegatedUserId(0L);

        Optional<Todo> optionalHighTodo = todoRepository.findTopByUserIdAndPriority(todoDto.getUserId(), SortBy.ASC.isAsc(), Priority.HIGH, todoDto.getPracticeDate());
        if (optionalHighTodo.isEmpty()) { // high 0 초기화
            todoDto.setPriority(Priority.HIGH);
            todoDto.setPriorityValue(DEFAULT_PRIORITY_VALUE);
        } else { // 가장 낮은 우선순위 가져오기
            Optional<Todo> optionalLowestTodo = todoRepository.findTopByUserId(todoDto.getUserId(), SortBy.DESC.isAsc(), todoDto.getPracticeDate());
            if (optionalLowestTodo.isEmpty()) {
                throw new RuntimeException("해당 태스크가 존재하지 않습니다.");
            }
            Todo lowestTodo = optionalLowestTodo.get();
            todoDto.setPriority(lowestTodo.getPriority());
            todoDto.setPriorityValue(lowestTodo.getPriorityValue() + NEXT_PRIORITY_VALUE);
        }
    }

    // 위임 거절
    //@Transactional
    public void restore(long todoId) {
        Todo delegatedTodo = getTodoDtoById(todoId);
        Todo prevTodo = getTodoDtoById(delegatedTodo.getPrevTodoId());

        // 원래 태스크 복구
        prevTodo.restore();
        sortPriorityByIncreasing(prevTodo.toDto());
        todoRepository.save(prevTodo);

        // 위임 거절한 태스크
        sortPriorityByReducing(delegatedTodo.toDto());
        delegatedTodo.delete();
        todoRepository.save(delegatedTodo);
    }

    //@Transactional
    public TodoDto changePriority(TodoDto todoDto) { // 중복되거나 누락이 있어서는 안된다.
        Todo todo = getTodoDtoById(todoDto.getId());
        // 변경하고자 하는 등급을 가진 태스크가 없으면 디폴트 값으로 등록
        Optional<Todo> optionalTodo = todoRepository.findTopByUserIdAndPriority(todo.getUserId(), SortBy.ASC.isAsc(), todoDto.getPriority(), todo.getPracticeDate());
        if (optionalTodo.isEmpty()) {
            if (todoDto.getPriorityValue() != DEFAULT_PRIORITY_VALUE) {
                throw new RuntimeException("등록할 수 없는 우선순위입니다.");
            }
        } else { // 변경하고자 하는 등급을 가진 태스크가 있으면 변경 가능한지 확인
            Todo prevTodo = optionalTodo.get();
            if (!todoDto.isChangeablePriority(prevTodo.getPriorityValue(), todo.getPriority())) {
                throw new RuntimeException("등록할 수 없는 우선순위입니다.");
            }
        }
        // 중간에 끼워넣기 식으로 변경은 불가능
        sortStatus(todoDto);
        todo.changePriority(todoDto);
        return todoRepository.save(todo).toDto();
    }

    // 상태 변경
    //@Transactional
    public TodoDto goNextStatus(long id) {
        Todo todo = getTodoDtoById(id);
        todo.goNextStatus();
        Todo nextTodo = todoRepository.save(todo);
        if (TodoStatus.DONE.equals(todo.getStatus())) {
            sortPriorityByReducing(todo.toDto()); // 같은 등급 내에서만 변경
        }
        return nextTodo.toDto();
    }

    //@Transactional
    public TodoDto cancel(long id) {
        Todo todo = getTodoDtoById(id);
        todo.cancel();
        Todo cancelledTodo = todoRepository.save(todo);
        sortPriorityByReducing(todo.toDto()); // 같은 등급 내에서만 변경
        return cancelledTodo.toDto();
    }

    // 삭제
    //@Transactional
    public void delete(long id) {
        Todo todo = getTodoDtoById(id);
        todo.delete();
        todoRepository.save(todo);
        sortPriorityByReducing(todo.toDto()); // 같은 등급 내에서만 변경
    }

    // 상태 정렬 메소드
    private void sortStatus(TodoDto todoDto) {
        // 변경 전 등급
        // 해당 상태보다 높은 상태를 가진 태스크들의 우선순위를 1씩 감소시킨다.
        Todo prevTodo = getTodoDtoById(todoDto.getId());
        sortPriorityByReducing(prevTodo.toDto());

        // 변경 후 등급
        // 해당 상태보다 높은 상태를 가진 태스크들의 상태를 1씩 증가시켜야 한다.
        sortPriorityByIncreasing(todoDto);
    }

    // 우선순위 변경 시, 해당 우선순위 이상 태스크들의 우선순위를 1씩 증가시켜야 한다.
    private void sortPriorityByIncreasing(TodoDto todoDto) {
        todoRepository.findGreaterThanEqualPriorityValue(todoDto.getUserId(), todoDto.getPriorityValue(), todoDto.getPriority(), todoDto.getPracticeDate())
                .forEach(t -> {
                            t.setPriorityValue(t.getPriorityValue() + NEXT_PRIORITY_VALUE);
                            todoRepository.save(t);
                        }
                );
    }

    // 우선순위 변경 시, 같은 등급에서 해당 우선순위보다 높은 우선순위를 가진 태스크들의 우선순위를 1씩 감소시켜야 한다.
    private void sortPriorityByReducing(TodoDto todoDto) {
        todoRepository.findGreaterThanPriorityValue(todoDto.getUserId(), todoDto.getPriorityValue(), todoDto.getPriority(), todoDto.getPracticeDate())
                .forEach(t -> {
                            t.setPriorityValue(t.getPriorityValue() - NEXT_PRIORITY_VALUE);
                            todoRepository.save(t);
                        }
                );
    }

    private Todo getTodoDtoById(long id) {
        Optional<Todo> optionalTodo = todoRepository.findByIdAndDeleted(id);
        if (optionalTodo.isEmpty()) {
            throw new RuntimeException("해당 태스크가 존재하지 않습니다.");
        }
        return optionalTodo.get();
    }
}
