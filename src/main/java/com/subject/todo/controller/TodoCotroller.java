package com.subject.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

    //특정 담당자, 특정 일자의 todo list 를 중요도/우선순위로 정렬하여 조회할 수 있다.
    @PostMapping("/todos")
    public List<SearchTodoResponse> search(
            @RequestBody SearchTodoRequest searchTodoRequest
    ) {
        List<TodoDto> todos = todoService.searchTodos(searchTodoRequest.toDto());
        return todoMapper.toResponseList(todos);
    }

}
