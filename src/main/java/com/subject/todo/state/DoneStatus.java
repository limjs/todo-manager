package com.subject.todo.state;

import com.subject.todo.code.TodoStatus;

public class DoneStatus implements TodoState{
    @Override
    public TodoStatus next() {
        throw new RuntimeException("다음 status가 없습니다.");
    }

    @Override
    public TodoStatus cancel() {
        throw new RuntimeException("취소 불가한 status입니다.");
    }
}
