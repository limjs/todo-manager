package com.subject.todo.repository;


import com.subject.state.TodoState;
import com.subject.todo.code.Priority;
import com.subject.todo.code.TodoStatus;
import com.subject.todo.service.TodoDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.subject.db.MemoryEntity;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Todo extends MemoryEntity {
    private Long id; // auto increment
    private LocalDate practiceDate; // todo를 실행할 날짜
    private Long userId; // todo를 실행할 유저의 id
    private Priority priority;// 우선순위 등급
    private Integer priorityValue; // 우선순위 값
    private String task; // 업무 제목
    private String description; // 업무 내용
    private TodoStatus status; // 업무 상태
    private Boolean delegation; // 위임 여부
    private Long delegationId; // 위임한 유저의 id
    private Long prevTodoId; // 위임전 태스크 id
    private Boolean isDeleted; // 삭제 여부

    public Todo(long id, LocalDate practiceDate, long userId, Priority priority, int priorityValue) {
        this.id = id;
        this.practiceDate = practiceDate;
        this.userId = userId;
        this.priority = priority;
        this.priorityValue = priorityValue;
        this.isDeleted = false;
    }

    public Todo(TodoDto todo) {
        this.status = TodoStatus.WAITING;
        this.delegation = false;
        this.priority = Priority.MEDIUM;
        this.priorityValue = 0;
        this.id = todo.getId();
        this.practiceDate = todo.getPracticeDate();
        this.userId = todo.getUserId();
        this.task = todo.getTask();
        this.description = todo.getDescription();
        this.delegationId = todo.getDelegationId();
        this.isDeleted = false;
    }

    public void changeStatus(TodoDto todoDto) {
        this.status = todoDto.getStatus();
    }

    public void changePriority(TodoDto todoDto) {
        this.priority = todoDto.getPriority();
        this.priorityValue = todoDto.getPriorityValue();
    }

    public void delegation(TodoDto todoDto) {
        this.delegation = Boolean.TRUE;
        this.delegationId = todoDto.getDelegationId();
        this.userId = todoDto.getUserId();
        this.priority = Priority.MEDIUM;
        this.priorityValue = todoDto.getPriorityValue();
    }

}
