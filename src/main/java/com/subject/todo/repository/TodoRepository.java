package com.subject.todo.repository;

import com.subject.todo.code.Priority;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import com.subject.db.MemoryRepositoryImpl;
import com.subject.todo.service.SearchTodoDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TodoRepository extends MemoryRepositoryImpl<Todo> {

    @PostConstruct
    public void init() {
        save(new Todo(1L, LocalDate.parse("2021-08-01"), 1L, Priority.HIGH, 1));
        save(new Todo(2L, LocalDate.parse("2021-08-01"), 1L, Priority.MEDIUM, 1));;
        save(new Todo(3L, LocalDate.parse("2021-08-01"), 1L, Priority.MEDIUM, 0));;
        save(new Todo(4L, LocalDate.parse("2021-08-01"), 1L, Priority.MEDIUM, 2));
        save(new Todo(5L, LocalDate.parse("2021-08-01"), 2L, Priority.HIGH, 1));
        save(new Todo(6L, LocalDate.parse("2021-08-01"), 3L, Priority.HIGH, 1));
    }

    public List<Todo> searchTodos(SearchTodoDto searchTodoDto) {
        return  findAll().stream()
                .filter(todo -> todo.getPracticeDate().equals(searchTodoDto.getPracticeDate()))
                .filter(todo -> todo.getUserId() == searchTodoDto.getId())
                .sorted((o1, o2) -> {
                    if (searchTodoDto.getSort() == 1) {
                        if (o1.getPriority().getOrder() == o2.getPriority().getOrder()) {
                            return o1.getPriorityValue().compareTo(o2.getPriorityValue());
                        }
                        return o1.getPriority().getOrder() > o2.getPriority().getOrder() ? 1 : -1;
                    } else { // reverse
                        if (o1.getPriority().getOrder() == o2.getPriority().getOrder()) {
                            return o2.getPriorityValue().compareTo(o1.getPriorityValue());
                        }
                        return o1.getPriority().getOrder() < o2.getPriority().getOrder() ? 1 : -1;
                    }
                })
                .collect(Collectors.toList());
    }
}
