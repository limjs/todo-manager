package com.subject.todo.controller;

import com.subject.todo.code.TodoStatus;
import lombok.Getter;
import lombok.Setter;
import com.subject.todo.code.Priority;

import java.time.LocalDate;

@Getter
@Setter
public class SearchTodoResponse {
    private long id;
    private LocalDate practiceDate; // todo를 실행할 날짜
    private long userId; // todo를 실행할 유저의 id
    private Priority priority; // 우선순위 등급
    private int priorityValue; // 우선순위 값
    private String task; // 업무 제목
    private String description; // 업무 내용
    private TodoStatus status; // 업무 상태
    private boolean delegation; // 위임 여부
    private long delegationId; // 위임한 유저의 id
}
