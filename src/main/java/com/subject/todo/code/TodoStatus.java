package com.subject.todo.code;

import com.subject.todo.state.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TodoStatus {
    // next와 취소가 가능한 상태
    WAITING("대기", new WaitingStatus()),
    IN_PROGRESS("진행중", new InProgressStatus()),
    // 변경 불가능한 상태
    DONE("완료", new DoneStatus()),
    CANCEL("취소", new CancelState());

    private String description;
    private TodoState todoState;

    public static boolean isTodo(TodoStatus todoStatus) {
        return switch (todoStatus) {
            case DONE, CANCEL -> false;
            default -> true;
        };
    }
}
