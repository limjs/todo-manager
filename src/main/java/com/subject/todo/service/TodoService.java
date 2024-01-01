package com.subject.todo.service;

import com.subject.todo.code.Priority;
import com.subject.todo.code.TodoStatus;
import com.subject.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.subject.todo.TodoMapper;
import com.subject.todo.repository.Todo;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;
    private final Integer NEXT_PRIORITY_VALUE = 1;

    @Autowired
    public TodoService(TodoRepository todoRepository, TodoMapper todoMapper) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
    }

    // 특정 담당자, 특정 일자의 todo list 를 중요도/우선순위로 정렬하여 조회할 수 있다.
    public List<TodoDto> searchTodos(SearchTodoDto searchTodoDto) {
        List<Todo> todos = todoRepository.searchTodos(searchTodoDto);
        return todoMapper.toDtoList(todos);
    }

    // 생성
    public TodoDto create(TodoDto todoDto) throws Exception {
        Optional<Todo> optionalTodo = todoRepository.findTopByUserIdAndPriority(todoDto.getUserId(),true, Priority.MEDIUM);
        if (optionalTodo.isEmpty()) {
            throw new Exception("해당 todo가 존재하지 않습니다.");
        }
        Todo prevTodo = optionalTodo.get();
        // medium 의 가장 마지막 우선순위로 등록됨
        Todo todo = getTodoDtoById(todoDto.getId());
        todoDto.setPriorityValue(prevTodo.getPriorityValue() + NEXT_PRIORITY_VALUE);
       // todoDto.createEntity(prevTodo.getPriorityValue());
        return todoMapper.toDto(todoRepository.save(todoDto.toEntity()));
    }

    // 위임
    // 위임한 담당자에게 todo는 위임 + 위임받은 사람이 표시된다.
    public TodoDto delegate(long todoId, long userId) throws Exception {
        TodoDto todoDto = todoMapper.toDto(getTodoDtoById(todoId));
        if (!todoDto.isDelegation()) { // 위임이 가능한 태스크인가?
            throw new Exception("이미 위임된 태스크입니다.");
        }
        // 위임받는 사람에게 이미 high task가 존재하는가? 그렇다면 가장 낮은 우선순위로 저장해라.
        // set priority & value
        Optional<Todo> optionalTodo = todoRepository.findTopByUserIdAndPriority(todoDto.getUserId(),true, Priority.HIGH);
        if (optionalTodo.isEmpty()) {
            throw new Exception("해당 todo가 존재하지 않습니다.");
        }
        Todo prevTodo = optionalTodo.get();
        // high 0 or 가장 낮은 우선순위 가져오기
        if (prevTodo.getPriorityValue() != 0) {
            Optional<Todo> optionalLowestTodo = todoRepository.findTopByUserId(userId, false);
            if (optionalLowestTodo.isEmpty()) {
                throw new Exception("해당 todo가 존재하지 않습니다.");
            }
            Todo lowestTodo = optionalLowestTodo.get();
            todoDto.setPriority(lowestTodo.getPriority());
            todoDto.setPriorityValue(lowestTodo.getPriorityValue() + NEXT_PRIORITY_VALUE);
        } else {
            todoDto.setPriority(Priority.HIGH);
            todoDto.setPriorityValue(0);
        }
        todoDto.delegation(userId);
        // 위임한 태스크 -> 삭제, 위임여부 update
        // 위임받은 태스크 -> 새로 생성, 위임전 태스크 id 저장
        return todoMapper.toDto(todoRepository.save(todoDto.toEntity()));
    }

    public TodoDto changePriority(TodoDto todoDto) throws Exception {
        // 변경 가능한 등급인지 확인. 중복되거나 누락이 있어서는 안된다.
        // 해당 등급의 가장 낮은 우선순위 가져옴. 그거랑 1 차이어야 등록 가능함.
        Optional<Todo> optionalTodo = todoRepository.findTopByUserIdAndPriority(todoDto.getUserId(),true, todoDto.getPriority());
        if (optionalTodo.isEmpty()) {
            throw new Exception("해당 todo가 존재하지 않습니다.");
        }
        Todo prevTodo = optionalTodo.get();
        Todo todo = getTodoDtoById(todoDto.getId());

        todoDto.changePriority(prevTodo.getPriority(), prevTodo.getPriorityValue());
        return todoMapper.toDto(todoRepository.save(todoDto.toEntity()));
    }

/*    // 상태 변경
    // 이동 가능한 상태가 정해져있따.
    public TodoDto goNextStatus(long id) throws Exception {
        Todo todo = getTodoDtoById(id);
        todo.goNextStatus();
        return todoMapper.toDto(todoRepository.save(todo));
    }

    public void cancel(long id) {
        // cancel
        Todo todo = getTodoDtoById(id);
        todo.cancel();
        todoRepository.save(todo);
    }

    // 삭제
    public void delete(long id) {
        Todo todo = getTodoDtoById(id);
        todo.delete();
        todoRepository.save(todo);
    }

    // todo 위임 거절. 원상복구.
    // 2) 위임 key를 넣어주는 것이 맞는가? -> 위임/위임거절 시 해당 key로 이전 상태 태스크를 찾을 수 있음. 관리 컬럼 수가 늘어남.
    // 2-1) 위임할 때. 위임한 사람의 태스크를 복사해서 새롭게 태스크를 생성하면?
    //  -> 해당 태스크는 위임으로 표시(이래서 원래 주인한테서 태스크 삭제하면 안됨). 새로 만들어진 태스크는 거절하면 복귀됨.
    //  -> 복귀 후의 entity 상태는 어떻게? 삭제 처리?
    public void restore(long id) {
        // 위임 거절한 태스크 -> 삭제
        // 위임 다시 돌려받은 태스크 -> 삭제 복구(?) or 새로 생성, 원상태로 복구.
        Todo todo = getTodoDtoById(id);
        Todo delegationTodo = getTodoDtoById(todo.getDelegationId());

        todo.delete();
        delegationTodo.restore(); // 복구

        todoRepository.save(todo);
        todoRepository.save(delegationTodo);
    }
*/

    private Todo getTodoDtoById(long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isEmpty()) {
            // throw new Exception("해당 todo가 존재하지 않습니다.");
        }
        return optionalTodo.get();
    }
}
