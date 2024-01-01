package com.subject.todo.controller.model;


import com.subject.todo.code.Priority;
import com.subject.todo.code.TodoStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class TodoResponse {
    private Long id; // auto increment
    private LocalDate practiceDate; // todo를 실행할 날짜
    private Long userId; // todo를 실행할 유저의 id
    private Priority priority;// 우선순위 등급
    private Integer priorityValue; // 우선순위 값
    private String task; // 업무 제목
    private String description; // 업무 내용
    private TodoStatus status; // 업무 상태
    private Boolean isDeleted; // 삭제 여부
    private Long prevUserId;// 위임한 유저의 id
    private Long prevTodoId; // 위임전 태스크 id
    private Long delegatedUserId; // 위임받은 유저의 id
    private Boolean isDelegated; // 위임 여부
}
