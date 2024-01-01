package com.subject.todo.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Priority {
    HIGHEST("매우높음", 0),
    HIGH("높음", 1),
    MEDIUM("중간", 2),
    LOW("낮음", 3);

    private String description;

    private int order;
}
