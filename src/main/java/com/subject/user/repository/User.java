package com.subject.user.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.subject.db.MemoryEntity;

@Data
@RequiredArgsConstructor
public class User extends MemoryEntity {
    private String name;

    public User(String name) {
        this.name = name;
    }
}
