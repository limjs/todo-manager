package com.subject.user.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import com.subject.db.MemoryRepositoryImpl;

import java.util.List;

@Repository
public class UserRepository extends MemoryRepositoryImpl<User> {
    @PostConstruct
    public void init() {
        save(new User(1L, "김철수"));
        save(new User(2L, "이훈이"));
        save(new User(3L, "한유리"));
        save(new User(4L, "박영희"));
        save(new User(5L, "홍길동"));
    }

    public List<User> findByUserName(String name) {
        return findAll().stream()
                .filter(user -> user.getName().equals(name))
                .toList();
    }
}
