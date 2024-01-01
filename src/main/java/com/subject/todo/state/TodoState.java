package com.subject.todo.state;

import com.subject.todo.code.TodoStatus;

public interface TodoState {
    TodoStatus next() throws Exception;
    TodoStatus cancel() throws Exception;
}
