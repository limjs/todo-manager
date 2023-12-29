package com.subject.db;

import java.util.List;
import java.util.Optional;

public interface MemoryRepository<T> {
    T save(T entity);

    Optional<T> findById(Long id);

    long deleteById(Long id);

    List<T> findAll();
}
