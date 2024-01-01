package com.subject.todo.controller.model;

import com.subject.todo.code.SortBy;
import com.subject.todo.service.model.SearchTodoDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchTodoRequest {
    private String name;
    private LocalDate practiceDate;
    private SortBy sortBy;

    public SearchTodoDto toDto() {
        return SearchTodoDto.builder()
                .id(1L)
                .name(this.getName())
                .practiceDate(this.getPracticeDate())
                .sortBy(this.getSortBy())
                .build();
    }
}
