package com.subject.todo.controller.model;

import com.subject.todo.service.model.TodoDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TodoRequest {
    @NotBlank
    private String task; // 업무 제목
    private String description; // 업무 내용
    @NotNull
    private Long userId; // todo를 실행할 유저의 id
    @NotNull
    private LocalDate practiceDate; // todo를 실행할 날짜

    public TodoDto toDto () {
        return TodoDto.builder()
                .task(this.task)
                .description(this.description)
                .userId(this.userId)
                .practiceDate(this.practiceDate)
                .build();
    }

}
