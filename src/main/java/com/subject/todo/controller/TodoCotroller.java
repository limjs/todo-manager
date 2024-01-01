package com.subject.todo.controller;

import com.subject.todo.controller.model.*;
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
            @RequestBody SearchTodoRequest searchTodoRequest
    ) {
        List<TodoDto> todos = todoService.searchTodos(searchTodoRequest.toDto());
        return todoMapper.toSearchResponseList(todos);
    }

    @PostMapping("/todo")
    public TodoResponse create(
            @RequestBody TodoRequest request
    ) {
        TodoDto todoDto = null;
        try {
            todoDto = todoService.create(request.toDto());
        } catch (Exception e) {
            log.info("error: {}", e.getMessage());
        }
        return todoDto.toResponse();
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
            log.info("error: {}", e.getMessage());
        }
        return dto.toResponse();
    }

    @PatchMapping("/todo/{todoId}/rejection")
    public void restore(
            @PathVariable("todoId") Long todoId
    ) {
        try {
            todoService.restore(todoId);
        } catch (Exception e) {
            log.info("error: {}", e.getMessage());
        }
    }

    @PostMapping("/todo/{todoId}/priority")
    public TodoResponse changePriority(
            @PathVariable("todoId") Long todoId,
            @RequestBody PriorityTodoRequest todorequest
    ) {
        TodoDto todoDto = null;
        try {
            todoDto = todoService.changePriority(todorequest.toDto(), todoId);
        } catch (Exception e) {
            log.info("error: {}", e.getMessage());
        }
        return todoDto.toResponse();
    }

    @PatchMapping("/todo/{todoId}/status")
    public TodoResponse goNextStatus(
            @PathVariable("todoId") Long todoId
    ) {
        TodoDto dto = null;
        try {
            dto = todoService.goNextStatus(todoId);
        } catch (Exception e) {
            log.info("error: {}", e.getMessage());
        }
        return dto.toResponse();
    }

    @PatchMapping("/todo/{todoId}/cancel")
    public TodoResponse cancel(
            @PathVariable("todoId") Long todoId
    ) {
        TodoDto dto = null;
        try {
            dto = todoService.cancel(todoId);
        } catch (Exception e) {

        }
        return dto.toResponse();
    }

    @DeleteMapping("/todo/{todoId}")
    public void delete(
            @PathVariable("todoId") Long todoId
    ) {
        try {
            todoService.delete(todoId);
        } catch (Exception e) {

        }
    }


    @GetMapping("/todos/all")
    public List<TodoResponse> searchTest() {
        List<TodoDto> todos = todoService.searchAllTodos();
        return todoMapper.toResponseList(todos);
    }
    @PostMapping("/test/dummy")
    public void createDummyData() {
        // 테스트 데이터 생성
        todoService.createDummyData();
    }
}
