package com.subject.todo.repository;


import com.subject.todo.code.Priority;
import com.subject.todo.code.TodoStatus;
import com.subject.todo.service.model.TodoDto;
import lombok.*;
import com.subject.db.MemoryEntity;

import java.time.LocalDate;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Todo extends MemoryEntity {
    private LocalDate practiceDate; // todo를 실행할 날짜
    private Long userId; // 담당 유저 id
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

    public Todo(LocalDate practiceDate, long userId, Priority priority, int priorityValue) {
        this.practiceDate = practiceDate;
        this.userId = userId;
        this.priority = priority;
        this.priorityValue = priorityValue;
        this.isDeleted = false;
        this.prevTodoId = 0L;
        this.prevUserId = 0L;
        this.delegatedUserId = 0L;
        this.status = TodoStatus.WAITING;
        this.isDelegated = false;
    }

    public void changePriority(TodoDto todoDto) {
        this.priority = todoDto.getPriority();
        this.priorityValue = todoDto.getPriorityValue();
    }

    // 위임한 태스크
    public void delegate(long delegatedUserId) {
        this.delegatedUserId = delegatedUserId;
        this.isDelegated = Boolean.TRUE;
    }

    // 위임 거절 시 원 태스크 복구
    public void restore() {
        this.delegatedUserId = null;
        this.isDelegated = false;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void goNextStatus() {
        this.status = this.status.getTodoState().next();
    }

    public void cancel() {
        this.status = this.status.getTodoState().cancel();
    }

    public TodoDto toDto() {
        return TodoDto.builder()
                .id(this.getId())
                .practiceDate(this.practiceDate)
                .userId(this.userId == null ? 0 : this.userId)
                .priority(this.priority)
                .priorityValue(this.priorityValue)
                .task(this.task)
                .description(this.description)
                .status(this.status)
                .isDeleted(this.isDeleted)
                .prevUserId(this.prevUserId == null ? 0 : this.prevUserId)
                .prevTodoId(this.prevTodoId == null ? 0 : this.prevTodoId)
                .delegatedUserId(this.delegatedUserId == null ? 0 : this.delegatedUserId)
                .isDelegated(this.isDelegated == null ? false : this.isDelegated)
                .build();
    }
}
