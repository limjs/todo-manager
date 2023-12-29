package com.subject.todo.code;

import lombok.Getter;

@Getter
public enum TodoStatus {
    WAITING("대기"),
    IN_PROGRESS("진행중"),
    DONE("완료"),
    CANCEL("취소");

    @Getter
    private String description;

    TodoStatus(String description) {
        this.description = description;
    }
}
