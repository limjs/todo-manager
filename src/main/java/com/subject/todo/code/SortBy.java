package com.subject.todo.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortBy {
    ASC("오름차순", true),
    DESC("내림차순", false);

    private String description;
    private boolean isAsc;
}
