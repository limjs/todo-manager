package com.subject.todo.service;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class SearchTodoDto {
    private long id;
    private String name;
    private LocalDate practiceDate;
    private int sort; // 오름차순 또는 내림차순 정렬 선택
}
