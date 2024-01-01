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

import java.time.LocalDate;
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

    public List<TodoDto> searchAllTodos() {
        List<Todo> todos = todoRepository.findAll();
        return todoMapper.toDtoList(todos);
    }

    // 특정 담당자, 특정 일자의 list 를 중요도/우선순위로 정렬하여 조회할 수 있다.
    public List<TodoDto> searchTodos(SearchTodoDto searchTodoDto) {
        List<Todo> todos = todoRepository.searchTodos(searchTodoDto);
        return todoMapper.toDtoList(todos);
    }

    // 생성
    public TodoDto create(TodoDto todoDto) throws Exception {
        // medium 등급의 가장 마지막 우선순위
        Optional<Todo> optionalTodo = todoRepository.findTopByUserIdAndPriority(todoDto.getUserId(), SortBy.ASC.isAsc(), Priority.MEDIUM);
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
    public TodoDto delegate(long todoId, long delegatedUserId) throws Exception {
        Todo prevTodo = getTodoDtoById(todoId);
        if (prevTodo.getIsDelegated()) {
            throw new Exception("이미 위임된 태스크입니다.");
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
    private void setDelegatedTask(TodoDto todoDto) throws Exception {
        todoDto.setPrevUserId(todoDto.getUserId());
        todoDto.setUserId(todoDto.getDelegatedUserId());
        todoDto.setStatus(TodoStatus.WAITING);
        todoDto.setDeleted(Boolean.FALSE);
        todoDto.setPrevTodoId(todoDto.getId());
        todoDto.setDelegated(Boolean.FALSE);
        todoDto.setDelegatedUserId(0L);

        Optional<Todo> optionalHighTodo = todoRepository.findTopByUserIdAndPriority(todoDto.getUserId(), SortBy.ASC.isAsc(), Priority.HIGH);
        if (optionalHighTodo.isEmpty()) { // high 0 초기화
            todoDto.setPriority(Priority.HIGH);
            todoDto.setPriorityValue(DEFAULT_PRIORITY_VALUE);
        } else { // 가장 낮은 우선순위 가져오기
            Optional<Todo> optionalLowestTodo = todoRepository.findTopByUserId(todoDto.getUserId(), SortBy.DESC.isAsc());
            if (optionalLowestTodo.isEmpty()) {
                throw new Exception("해당 todo가 존재하지 않습니다.");
            }
            Todo lowestTodo = optionalLowestTodo.get();
            todoDto.setPriority(lowestTodo.getPriority());
            todoDto.setPriorityValue(lowestTodo.getPriorityValue() + NEXT_PRIORITY_VALUE);
        }
    }

    // 위임 거절
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

    private void sortPriorityByIncreasing(TodoDto todoDto) {
        todoRepository.findGreaterThanEqualPriorityValue(todoDto.getUserId(), todoDto.getPriorityValue(), todoDto.getPriority())
                .forEach(t -> {
                            t.setPriorityValue(t.getPriorityValue() + NEXT_PRIORITY_VALUE);
                            todoRepository.save(t);
                        }
                );
    }

    // 우선순위 변경 시, 해당 우선순위보다 높은 우선순위를 가진 태스크들의 우선순위를 1씩 감소시켜야 한다.
    private void sortPriorityByReducing(TodoDto todoDto) {
        todoRepository.findGreaterThanPriorityValue(todoDto.getUserId(), todoDto.getPriorityValue(), todoDto.getPriority())
                .forEach(t -> {
                            t.setPriorityValue(t.getPriorityValue() - NEXT_PRIORITY_VALUE);
                            todoRepository.save(t);
                        }
                );
    }

    public TodoDto changePriority(TodoDto todoDto, long todoId) throws Exception {
        Todo todo = getTodoDtoById(todoId);
        // 변경 가능한 등급인지 확인. 중복되거나 누락이 있어서는 안된다.
        Optional<Todo> optionalTodo = todoRepository.findTopByUserIdAndPriority(todoDto.getUserId(), SortBy.ASC.isAsc(), todoDto.getPriority());
        if (optionalTodo.isEmpty()) { // 변경하고자 하는 등급이 없으면 디폴트로 등록
            if (todoDto.getPriorityValue() != DEFAULT_PRIORITY_VALUE) {
                throw new Exception("등록할 수 없는 우선순위입니다.");
            }
        } else {
            Todo prevTodo = optionalTodo.get();
            if (!todoDto.isChangeablePriority(prevTodo.getPriorityValue(), todo.getPriority())) {
                throw new Exception("등록할 수 없는 우선순위입니다.");
            }
        }
        todo.changePriority(todoDto);
        return todoRepository.save(todo).toDto();
    }

    // 상태 변경
    public TodoDto goNextStatus(long id) throws Exception {
        Todo todo = getTodoDtoById(id);
        todo.goNextStatus();
        return todoRepository.save(todo).toDto();
    }

    public TodoDto cancel(long id) throws Exception {
        Todo todo = getTodoDtoById(id);
        todo.cancel();
        return todoRepository.save(todo).toDto();
    }

    // 삭제
    public void delete(long id) {
        Todo todo = getTodoDtoById(id);
        todo.delete();
        todoRepository.save(todo);
    }

    private Todo getTodoDtoById(long id) {
        Optional<Todo> optionalTodo = todoRepository.findByIdAndDeleted(id);
        if (optionalTodo.isEmpty()) {
            // throw new Exception("해당 todo가 존재하지 않습니다.");
        }
        return optionalTodo.get();
    }

    public void createDummyData() {
        todoRepository.save(new Todo(LocalDate.parse("2024-08-01"), 1L, Priority.HIGH, 1));
        todoRepository.save(new Todo(LocalDate.parse("2024-08-01"), 1L, Priority.MEDIUM, 1));
        todoRepository.save(new Todo(LocalDate.parse("2024-08-01"), 1L, Priority.MEDIUM, 0));
        todoRepository.save(new Todo(LocalDate.parse("2024-08-01"), 1L, Priority.MEDIUM, 2));
        todoRepository.save(new Todo(LocalDate.parse("2024-08-01"), 2L, Priority.HIGH, 0));
        todoRepository.save(new Todo(LocalDate.parse("2024-08-01"), 3L, Priority.HIGH, 0));
    }
}
