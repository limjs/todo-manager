package com.subject.todo.service.model;

import com.subject.todo.controller.model.TodoResponse;
import org.springframework.stereotype.Component;
import com.subject.todo.controller.model.SearchTodoResponse;
import com.subject.todo.repository.Todo;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TodoMapper {
    public List<TodoDto> toDtoList(List<Todo> todos) {
        return todos.stream()
                .map(t -> t.toDto())
                .collect(Collectors.toList());
    }

    public List<TodoResponse> toResponseList(List<TodoDto> todoDtos) {
        return todoDtos.stream()
                .map(t -> t.toResponse())
                .collect(Collectors.toList());
    }

    public List<SearchTodoResponse> toSearchResponseList(List<TodoDto> todoDtos) {
        return todoDtos.stream()
                .map(t -> t.toSearchResponse())
                .collect(Collectors.toList());
    }
}
