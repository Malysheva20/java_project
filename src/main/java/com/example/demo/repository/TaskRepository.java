package com.example.demo.repository;

import com.example.demo.model.Task;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TaskRepository {
    private final Map<Long, Task> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public Task save(Task t) {
        if (t.getId() == null) t.setId(idGen.getAndIncrement());
        if (t.getCreatedAt() == null) t.setCreatedAt(LocalDateTime.now());
        store.put(t.getId(), t);
        return t;
    }

    public Optional<Task> findById(Long id) {
        Task t = store.get(id);
        return (t == null || t.isDeleted()) ? Optional.empty() : Optional.of(t);
    }

    public List<Task> findAllByUser(Long userId) {
        return store.values().stream()
                .filter(t -> !t.isDeleted() && Objects.equals(t.getUserId(), userId))
                .toList();
    }

    public List<Task> findPendingByUser(Long userId) {
        return store.values().stream()
                .filter(t -> !t.isDeleted() && !t.isCompleted() && Objects.equals(t.getUserId(), userId))
                .toList();
    }

    public void softDelete(Long id) {
        Task t = store.get(id);
        if (t != null) t.setDeleted(true);
    }
}
