package com.subject.todo.code;

import lombok.Getter;

public enum Priority {
    HIGHEST("매우높음", 0),
    HIGH("높음", 1),
    MEDIUM("중간", 2),
    LOW("낮음", 3);

    @Getter
    private String description;

    @Getter
    private int order;

    Priority(String description, int order) {
        this.description = description;
        this.order = order;
    }


}
