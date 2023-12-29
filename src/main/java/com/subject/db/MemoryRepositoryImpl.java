package com.subject.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoryRepositoryImpl<T extends MemoryEntity> implements MemoryRepository<T>{

    private final List<T> memoryDB = new ArrayList<>();

    @Override
    public T save(T entity) {
        Optional<T> optionalT = memoryDB.stream()
                .filter(i -> i.getIndex().equals(entity.getIndex())).findFirst();

        if (optionalT.isPresent()) {
            long id = optionalT.get().getIndex();
            deleteById(id);
        } else {
            entity.setIndex((long) (memoryDB.size() + 1));
        }
        memoryDB.add(entity);
        return entity;
    }

    @Override
    public Optional<T> findById(Long id) {
        return memoryDB.stream().filter(i -> i.getIndex().equals(id)).findFirst();
    }

    @Override
    public long deleteById(Long id) {
        Optional<T> optionalT = memoryDB.stream()
                .filter(i -> i.getIndex().equals(id)).findFirst();

        if (optionalT.isPresent()) {
            memoryDB.remove(optionalT.get());
            return 1;
        }
        return 0;
    }

    @Override
    public List<T> findAll() {
        return memoryDB;
    }
}
