package com.subject.todo.controller.model;


import com.subject.todo.code.Priority;
import com.subject.todo.service.model.TodoDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PriorityTodoRequest {
    @NotNull
    private Priority priority; // 우선순위 등급
    @NotNull
    private Integer priorityValue; // 우선순위 값

    public TodoDto toDto () {
        return TodoDto.builder()
                .priority(this.priority)
                .priorityValue(this.priorityValue)
                .build();
    }

}
