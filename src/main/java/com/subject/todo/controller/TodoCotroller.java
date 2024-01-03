package com.subject.todo.controller;

import com.subject.todo.controller.model.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.subject.todo.service.model.TodoMapper;
import com.subject.todo.service.model.TodoDto;
import com.subject.todo.service.TodoService;

import java.util.List;

@Slf4j
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
            @Valid
            @RequestBody SearchTodoRequest searchTodoRequest
    ) {
        List<TodoDto> todos = todoService.searchTodos(searchTodoRequest.toDto());
        return todoMapper.toSearchResponseList(todos);
    }

    @PostMapping("/todo")
    public TodoResponse create(
            @Valid
            @RequestBody TodoRequest request
    ) {
        TodoDto todoDto = todoService.create(request.toDto());
        return todoDto.toResponse();
    }

    @PatchMapping("/todo/{todoId}/delegation/{userId}")
    public TodoResponse delegate(
            @PathVariable("todoId") Long todoId,
            @PathVariable("userId") Long userId
    ) {
        TodoDto dto = todoService.delegate(todoId, userId);
        return dto.toResponse();
    }

    @PatchMapping("/todo/{todoId}/rejection")
    public void restore(
            @PathVariable("todoId") Long todoId
    ) {
        todoService.restore(todoId);
    }

    @PostMapping("/todo/{todoId}/priority")
    public TodoResponse changePriority(
            @PathVariable("todoId") Long todoId,
            @Valid
            @RequestBody PriorityTodoRequest request
    ) {
        TodoDto requestDto = request.toDto();
        requestDto.setId(todoId);
        TodoDto todoDto = todoService.changePriority(requestDto);
        return todoDto.toResponse();
    }

    @PatchMapping("/todo/{todoId}/status")
    public TodoResponse goNextStatus(
            @PathVariable("todoId") Long todoId
    ) {
        TodoDto dto = todoService.goNextStatus(todoId);
        return dto.toResponse();
    }

    @PatchMapping("/todo/{todoId}/cancel")
    public TodoResponse cancel(
            @PathVariable("todoId") Long todoId
    ) {
        TodoDto dto = todoService.cancel(todoId);
        return dto.toResponse();
    }

    @DeleteMapping("/todo/{todoId}")
    public void delete(
            @PathVariable("todoId") Long todoId
    ) {
        todoService.delete(todoId);
    }

    @GetMapping("/todos/all")
    public List<TodoResponse> searchTest() {
        List<TodoDto> todos = todoService.searchAllTodos();
        return todoMapper.toResponseList(todos);
    }
}
