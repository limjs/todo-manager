package com.subject.todo.service;

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

    @Autowired
    public TodoService(TodoRepository todoRepository, TodoMapper todoMapper) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
    }


    // 특정 담당자, 특정 일자의 todo list 를 중요도/우선순위로 정렬하여 조회할 수 있다.
    public List<TodoDto> searchTodos(SearchTodoDto searchTodoDto) {
        List<Todo> todosa = todoRepository.findAll();
        log.info("todosa: {}", todosa);
        List<Todo> todos = todoRepository.searchTodos(searchTodoDto);
        log.info("todos: {}", todos);
        return todoMapper.toDtoList(todos);
    }

    // 생성
    public Todo create(TodoDto todoDto) {
        Todo todo = new Todo(todoDto);
        return todoRepository.save(todo);
    }

    // 우선순위, 순서 조정
    public Todo changePriority(TodoDto todoDto) {
        Todo todo = getTodoById(todoDto.getId());
        todo.changePriority(todoDto);
        return todoRepository.save(todo);
    }

    // 상태 변경
    public Todo changeStatus(TodoDto todoDto) {
        Todo todo = getTodoById(todoDto.getId());
        todo.changeStatus(todoDto);
        return todoRepository.save(todo);
    }

    // 삭제
    public void delete(long id) {
        todoRepository.deleteById(id);
    }


    // 위임
    public Todo delegation(TodoDto todoDto) {
        Todo todo = getTodoById(todoDto.getId());
        todo.delegation(todoDto);
        return todoRepository.save(todo);
    }

    // todo 위임 거절


    private Todo getTodoById(long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isEmpty()) {
            throw new IllegalArgumentException("해당 todo가 존재하지 않습니다.");
        }
        return optionalTodo.get();
    }
}
