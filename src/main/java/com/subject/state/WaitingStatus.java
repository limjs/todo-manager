package com.subject.state;

import com.subject.todo.code.TodoStatus;

public class WaitingStatus implements TodoState {

    @Override
    public TodoStatus next() {
        return TodoStatus.IN_PROGRESS;
    }

    @Override
    public TodoStatus cancel() {
        return TodoStatus.CANCEL;
    }
}
