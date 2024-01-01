package com.subject.state;

import com.subject.todo.code.TodoStatus;

public class InProgressStatus implements TodoState {
    @Override
    public TodoStatus next() {
        return TodoStatus.DONE;
    }

    @Override
    public TodoStatus cancel() {
        return TodoStatus.CANCEL;
    }
}
