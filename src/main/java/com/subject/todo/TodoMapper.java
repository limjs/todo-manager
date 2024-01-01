package com.subject.todo;

import com.subject.todo.controller.TodoResponse;
import com.subject.todo.service.TodoDto;
import org.springframework.stereotype.Component;
import com.subject.todo.controller.SearchTodoResponse;
import com.subject.todo.repository.Todo;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TodoMapper {
    // https://hstory0208.tistory.com/entry/SpirngJPA-Dto%EC%99%80-Entity%EB%A5%BC-%EB%B6%84%EB%A6%AC%ED%95%B4%EC%84%9C-%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0
    // mapper를 버릴까?

    public List<TodoDto> toDtoList(List<Todo> todos) {
        return todos.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TodoDto toDto(Todo todo) {
        TodoDto todoDto = new TodoDto();
        todoDto.setId(todo.getId());
        todoDto.setPracticeDate(todo.getPracticeDate());
        todoDto.setUserId(todo.getUserId());
        todoDto.setPriority(todo.getPriority());
        todoDto.setPriorityValue(todo.getPriorityValue());
        todoDto.setTask(todo.getTask());
        todoDto.setDescription(todo.getDescription());
        todoDto.setStatus(todo.getStatus());
     //   todoDto.setDelegation(todo.getDelegation());
     //   todoDto.setDelegationId(todo.getDelegationId());
        return todoDto;
    }


    public List<SearchTodoResponse> toResponseList(List<TodoDto> todoDtos) {
        return todoDtos.stream()
                .map(this::toSearchTodoDto)
                .collect(Collectors.toList());
    }

    public SearchTodoResponse toSearchTodoDto(TodoDto todoDto) {
        SearchTodoResponse searchTodoResponse = new SearchTodoResponse();
        searchTodoResponse.setId(todoDto.getId());
        searchTodoResponse.setPracticeDate(todoDto.getPracticeDate());
        searchTodoResponse.setUserId(todoDto.getUserId());
        searchTodoResponse.setPriority(todoDto.getPriority());
        searchTodoResponse.setPriorityValue(todoDto.getPriorityValue());
        searchTodoResponse.setTask(todoDto.getTask());
        searchTodoResponse.setDescription(todoDto.getDescription());
        searchTodoResponse.setStatus(todoDto.getStatus());
        searchTodoResponse.setDelegation(todoDto.isDelegation());
        searchTodoResponse.setDelegationId(todoDto.getDelegationId());
        return searchTodoResponse;
    }

    public TodoResponse toResponse (TodoDto todoDto) {
        TodoResponse todoResponse = new TodoResponse();
        return todoResponse;
    }

    public Todo toEntity(TodoDto todoDto) {
        Todo todo = new Todo();
        return todo;
    }
}
