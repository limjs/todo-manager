package com.subject.todo.controller.model;

import com.subject.todo.code.SortBy;
import com.subject.todo.service.model.SearchTodoDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SearchTodoRequest {
    @NotNull
    private Long userId;
    @NotNull
    private LocalDate practiceDate;
    @NotNull
    private SortBy sortBy;

    public SearchTodoDto toDto() {
        return SearchTodoDto.builder()
                .id(this.userId)
                .practiceDate(this.practiceDate)
                .sortBy(this.sortBy)
                .build();
    }
}
