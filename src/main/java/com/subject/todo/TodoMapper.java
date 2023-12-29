package com.subject.todo;

import com.subject.todo.service.TodoDto;
import org.springframework.stereotype.Component;
import com.subject.todo.controller.SearchTodoResponse;
import com.subject.todo.repository.Todo;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TodoMapper {

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

}
