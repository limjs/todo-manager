package com.subject.todo.service.model;

import com.subject.todo.code.SortBy;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class SearchTodoDto {
    private long id;
    private String name;
    private LocalDate practiceDate;
    private SortBy sortBy; // 오름차순 또는 내림차순 정렬 선택 -> enum
}
