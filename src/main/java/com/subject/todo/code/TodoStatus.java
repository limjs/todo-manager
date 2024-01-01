package com.subject.todo.code;

import com.subject.state.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;


// https://junuuu.tistory.com/819
@Getter
@AllArgsConstructor
public enum TodoStatus {
    // https://programmer-ririhan.tistory.com/412
    // 자기자신 리턴 안되는 이유

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
    /*
    WAITING("대기", ( bool ) -> IN_PROGRESS : CANCEL),
    IN_PROGRESS("진행중", ( bool ) -> bool ? DONE : CANCEL),
    DONE("완료"),
    CANCEL("취소");
*/

    // public Function<Boolean, TodoStatus> goToNext;

    // Condition이 불확실하니깐 이렇게는 안 쓰는 것이 좋다.
    /*public TodoStatus goToNext(TodoStatus prevStatus) {
        switch (prevStatus) {
            case WAITING:
                return "" ? DONE : CANCEL;
            case IN_PROGRESS:
            case DONE:
            case CANCEL:
            default:
        }
    }*/
