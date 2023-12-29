package com.subject.todo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class History {
    private long id;
    private long todoId; // todoEntity.getId()
    private long delegationId; // 위임한 사람
    private long userId; // 위임받은 사람
    private LocalDate delegationDate; // 위임한 날짜
}
