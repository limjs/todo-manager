package com.subject.todo.service;

import com.subject.state.TodoState;
import com.subject.todo.repository.Todo;
import lombok.Getter;
import lombok.Setter;
import com.subject.todo.code.Priority;
import com.subject.todo.code.TodoStatus;

import java.time.LocalDate;

@Getter
@Setter
public class TodoDto {
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
    private Long prevTodoId; // 위임전 태스크 id
    private TodoState todoState;
    private boolean isDeleted;

    public void delegation(long userId) {
        this.delegation = Boolean.TRUE;
        this.delegationId = this.userId;
        this.userId = userId;
        this.prevTodoId = this.id;
    }

    public void reject() {

    }

    public void changePriority(Priority nextPriority, int nextPriorityValue) {
        if (this.priorityValue != nextPriorityValue + 1){
            throw new IllegalArgumentException("등록할 수 없음");
        }
        this.priority = nextPriority;
        this.priorityValue = nextPriorityValue;
    }

    public void goNextStatus() throws Exception {
        this.status = this.status.getTodoState().next();
    }

    public void delete() {
        this.isDeleted = true;
    }

    // update 코드는 entity로 옮기자.
    // https://www.inflearn.com/questions/30076/jpa-create-%EB%B0%8F-update-%EC%8B%9C-dto%EC%97%90%EC%84%9C-entity-%EB%B3%80%ED%99%98%EB%B0%A9%EC%8B%9D
    public void createEntity(int priorityValue) {
        this.status = TodoStatus.WAITING;
        this.delegation = false;
        this.priority = Priority.MEDIUM;
        this.priorityValue = 0;
        this.isDeleted = false;
        this.priorityValue = priorityValue + 1; // medium 의 가장 마지막 우선순위로 등록됨
    }

    public Todo toEntity() {
        Todo todo = new Todo();
        return todo;
    }
}
