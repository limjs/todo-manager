package com.subject.todo.repository;

import com.subject.todo.code.Priority;
import com.subject.todo.code.TodoStatus;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import com.subject.db.MemoryRepositoryImpl;
import com.subject.todo.service.model.SearchTodoDto;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class TodoRepository extends MemoryRepositoryImpl<Todo> {

    @PostConstruct
    public void init() {
        save(new Todo(LocalDate.parse("2024-01-03"), 1L, Priority.HIGH, 1));
        save(new Todo(LocalDate.parse("2024-01-03"), 1L, Priority.MEDIUM, 0));
        save(new Todo(LocalDate.parse("2024-01-03"), 1L, Priority.MEDIUM, 1));
        save(new Todo(LocalDate.parse("2024-01-03"), 1L, Priority.MEDIUM, 2));
        save(new Todo(LocalDate.parse("2024-01-03"), 2L, Priority.HIGH, 0));
        save(new Todo(LocalDate.parse("2024-01-03"), 3L, Priority.HIGH, 0));
    }

    public List<Todo> searchTodos(SearchTodoDto searchTodoDto) {
        return (List<Todo>) getValidTodos(searchTodoDto.getPracticeDate()).stream()
                .filter(todo -> todo.getUserId() == searchTodoDto.getId())
                .sorted(sortedPriority(searchTodoDto.getSortBy().isAsc()))
                .collect(Collectors.toList());
    }

    public Optional<Todo> findByIdAndDeleted(long id) {
        return findAll().stream()
                .filter(todo -> todo.getId() == id)
                .filter(todo -> todo.getIsDeleted() == Boolean.FALSE)
                .findFirst();
    }

    // 유저가 보유한 task 중에서 우선순위가 가장 높거나 낮은 entity 가져오기
    public Optional<Todo> findTopByUserId(long userId, boolean isAsc, LocalDate practiceDate) {
        return getValidTodos(practiceDate).stream()
                .filter(todo -> todo.getUserId() == userId)
                .sorted(sortedPriority(isAsc)).findFirst();
    }

    // 유저가 보유한 task 중에서 등급별 가장 높거나 낮은 entity 가져오기
    public Optional<Todo> findTopByUserIdAndPriority(long userId, boolean isAsc, Priority priority, LocalDate practiceDate) {
        return getValidTodos(practiceDate).stream()
                .filter(todo -> todo.getUserId() == userId)
                .filter(todo -> todo.getPriority().equals(priority))
                .sorted(sortedPriority(isAsc))
                .findFirst();
    }

    public List<Todo> findGreaterThanPriorityValue(long userId, int priorityValue, Priority priority, LocalDate practiceDate) {
        return getValidTodos(practiceDate).stream()
                .filter(todo -> todo.getUserId() == userId)
                .filter(todo -> todo.getPriority().equals(priority))
                .filter(todo -> todo.getPriorityValue() > priorityValue) // 현재 등급보다 높은 등급의 태스크 필터링
                .collect(Collectors.toList());
    }

    public List<Todo> findGreaterThanEqualPriorityValue(long userId, int priorityValue, Priority priority, LocalDate practiceDate) {
        return getValidTodos(practiceDate).stream()
                .filter(todo -> todo.getUserId() == userId)
                .filter(todo -> todo.getPriority().equals(priority))
                .filter(todo -> todo.getPriorityValue() >= priorityValue) // 중간에 태스크를 끼워넣기 위해 현재 등급보다 높거나 같은 등급의 태스크 필터링
                .collect(Collectors.toList());
    }

    private List<Todo> getValidTodos(LocalDate practiceDate) {
        return findAll().stream()
                .filter(todo -> todo.getIsDeleted() == Boolean.FALSE && todo.getIsDelegated() == Boolean.FALSE) // 삭제&위임되지 않은 태스크 필터링
                .filter(todo -> TodoStatus.isTodo(todo.getStatus())) // 완료나 취소되지 않은 진행 가능한 태스크 필터링.
                .filter(todo -> todo.getPracticeDate().equals(practiceDate))
                .collect(Collectors.toList());
    }

    private Comparator sortedPriority(boolean isAsc) {
        return new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Todo t1 = (Todo) o1;
                Todo t2 = (Todo) o2;
                if (isAsc) { // 우선순위 오름차순 정렬
                    if (t1.getPriority().getOrder() == t2.getPriority().getOrder()) {
                        return t2.getPriorityValue().compareTo(t1.getPriorityValue());
                    }
                    return t1.getPriority().getOrder() < t2.getPriority().getOrder() ? 1 : -1;
                } else { // reverse
                    if (t1.getPriority().getOrder() == t2.getPriority().getOrder()) {
                        return t1.getPriorityValue().compareTo(t2.getPriorityValue());
                    }
                    return t1.getPriority().getOrder() > t2.getPriority().getOrder() ? 1 : -1;
                }
            }
        };
    }
}
