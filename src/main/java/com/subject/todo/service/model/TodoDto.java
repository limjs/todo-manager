package com.subject.todo.service.model;

import com.subject.todo.controller.model.SearchTodoResponse;
import com.subject.todo.controller.model.TodoResponse;
import com.subject.todo.state.TodoState;
import com.subject.todo.repository.Todo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.subject.todo.code.Priority;
import com.subject.todo.code.TodoStatus;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
@Slf4j
@Getter
@Setter
@Builder
public class TodoDto {
    private long id;
    private LocalDate practiceDate; // todo를 실행할 날짜
    private long userId; // todo를 실행할 유저의 id
    private Priority priority; // 우선순위 등급
    private int priorityValue; // 우선순위 값
    private String task; // 업무 제목
    private String description; // 업무 내용
    private TodoStatus status; // 업무 상태
    private boolean isDeleted;

    private long prevUserId; // 위임한 유저의 id
    private long prevTodoId; // 위임전 태스크 id
    private long delegatedUserId; // 위임받은 유저의 id
    private boolean isDelegated; // 위임 여부

    public boolean isChangeablePriority (int prevPriorityValue, Priority prevPriority) {
        if (this.priority == prevPriority) { // 같은 등급 내에서 변경 시 누락 발생
            log.info("같은 우선순위");
            return false;
        }
        if (this.priorityValue != prevPriorityValue + 1){
            log.info("우선순위 값이 1씩 차이나야 합니다.");
            return false;
        }
        return true;
    }

    public Todo createEntity(int priorityValue) {
        return Todo.builder()
                .practiceDate(this.practiceDate)
                .userId(this.userId)
                .priority(Priority.MEDIUM)
                .priorityValue(priorityValue)
                .task(this.task)
                .description(this.description)
                .status(TodoStatus.WAITING)
                .isDeleted(Boolean.FALSE)
                .prevUserId(0L)
                .prevTodoId(0L)
                .delegatedUserId(0L)
                .isDelegated(Boolean.FALSE)
                .build();
    }

    public Todo toEntity() {
        return Todo.builder()
                .practiceDate(this.practiceDate)
                .userId(this.userId)
                .priority(this.priority)
                .priorityValue(this.priorityValue)
                .task(this.task)
                .description(this.description)
                .status(this.status)
                .isDeleted(this.isDeleted)
                .prevUserId(this.prevUserId)
                .prevTodoId(this.prevTodoId)
                .delegatedUserId(this.delegatedUserId)
                .isDelegated(this.isDelegated)
                .build();
    }

    public TodoResponse toResponse() {
        return TodoResponse.builder()
                .id(this.id)
                .practiceDate(this.practiceDate)
                .userId(this.userId)
                .priority(this.priority)
                .priorityValue(this.priorityValue)
                .task(this.task)
                .description(this.description)
                .status(this.status)
                .isDeleted(this.isDeleted)
                .prevUserId(this.prevUserId)
                .prevTodoId(this.prevTodoId)
                .delegatedUserId(this.delegatedUserId)
                .isDelegated(this.isDelegated)
                .build();
    }

    public SearchTodoResponse toSearchResponse() {
        return SearchTodoResponse.builder()
                .id(this.id)
                .practiceDate(this.practiceDate)
                .userId(this.userId)
                .priority(this.priority)
                .priorityValue(this.priorityValue)
                .task(this.task)
                .description(this.description)
                .status(this.status)
                .isDeleted(this.isDeleted)
                .prevUserId(this.prevUserId)
                .prevTodoId(this.prevTodoId)
                .delegatedUserId(this.delegatedUserId)
                .isDelegated(this.isDelegated)
                .build();
    }
}
