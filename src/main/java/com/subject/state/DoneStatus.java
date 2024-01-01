package com.subject.state;

import com.subject.todo.code.TodoStatus;

public class DoneStatus implements TodoState{
    @Override
    public TodoStatus next() throws Exception {
        throw new Exception("");
    }

    @Override
    public TodoStatus cancel() throws Exception {
        throw new Exception("");
    }
}
