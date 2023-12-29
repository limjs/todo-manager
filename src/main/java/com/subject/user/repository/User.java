package com.subject.user.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.subject.db.MemoryEntity;

@Data
@RequiredArgsConstructor
public class User extends MemoryEntity {
    private Long id; // auto increment
    private String name;

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
