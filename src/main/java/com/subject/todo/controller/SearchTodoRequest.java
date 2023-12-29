package com.subject.todo.controller;

import com.subject.todo.service.SearchTodoDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchTodoRequest {
    private String name;
    private LocalDate practiceDate;
    private int sort;

    public SearchTodoDto toDto() {
        SearchTodoDto searchTodoDto = new SearchTodoDto();
        searchTodoDto.setId(1L);
        searchTodoDto.setName(this.getName());
        searchTodoDto.setPracticeDate(this.getPracticeDate());
        searchTodoDto.setSort(this.getSort());
        return searchTodoDto;
    }
}
