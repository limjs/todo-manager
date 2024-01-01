package com.subject.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.subject.todo.TodoMapper;
import com.subject.todo.service.TodoDto;
import com.subject.todo.service.TodoService;

import java.util.List;

@RestController
public class TodoCotroller {
    private final TodoService todoService;
    private final TodoMapper todoMapper;

    @Autowired
    public TodoCotroller(TodoService todoService, TodoMapper todoMapper) {
        this.todoService = todoService;
        this.todoMapper = todoMapper;
    }

    @PostMapping("/todos")
    public List<SearchTodoResponse> search(
            @RequestBody SearchTodoRequest searchTodoRequest
    ) {
        List<TodoDto> todos = todoService.searchTodos(searchTodoRequest.toDto());
        return todoMapper.toResponseList(todos);
    }

    @PostMapping("/todo")
    public TodoResponse create(
            @RequestBody TodoRequest todorequest
    ) {
        TodoDto todoDto = null;
        try {
            todoDto = todoService.create(todorequest.toDto());
        } catch (Exception e) {

        }
        return todoMapper.toResponse(todoDto);
    }

    @PatchMapping("/todo/{todoId}/delegation/{userId}")
    public TodoResponse delegate(
            @PathVariable("todoId") Long todoId,
            @PathVariable("userId") Long userId
    ) {
        TodoDto dto = null;
        try {
            dto = todoService.delegate(todoId, userId);
        } catch (Exception e) {

        }
        return todoMapper.toResponse(dto);
    }

    // 특정 todo 의 task, description, 우선순위, 상태를 변경할 수 있다.

}
