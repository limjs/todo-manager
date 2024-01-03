package com.subject.db;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class MemoryRepositoryImpl<T extends MemoryEntity> implements MemoryRepository<T>{

    private final List<T> memoryDB = new ArrayList<>();

    @Override
    public T save(T entity) {
        Optional<T> optionalT = memoryDB.stream()
                .filter(i -> i.getId().equals(entity.getId())).findFirst();
        long id = 0;
        if (optionalT.isPresent()) {
            id = optionalT.get().getId();
            entity.setId(id);
            deleteById(id);
        } else {
            id = memoryDB.size() + 1;
            entity.setId(id);
        }
        memoryDB.add(entity);
        return entity;
    }

    @Override
    public Optional<T> findById(Long id) {
        return memoryDB.stream().filter(i -> i.getId().equals(id)).findFirst();
    }

    @Override
    public long deleteById(Long id) {
        for (int i = 0; i < memoryDB.size(); i++) {
            if (Objects.equals(memoryDB.get(i).getId(), id)) {
                memoryDB.remove(i);
                return 1;
            }
        }
        return 0;
    }

    @Override
    public List<T> findAll() {
        return memoryDB;
    }
}
