package com.subject.todo.state;

import com.subject.todo.code.TodoStatus;

public class CancelState implements TodoState{
    @Override
    public TodoStatus next() throws Exception {
        throw new Exception("");
    }

    @Override
    public TodoStatus cancel() throws Exception {
        throw new Exception("");
    }
}
