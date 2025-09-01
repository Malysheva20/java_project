package com.example.demo.service.h2;

import com.example.demo.entity.TaskEntity;
import com.example.demo.model.Task;
import com.example.demo.repository.h2.TaskJpaRepository;
import com.example.demo.service.api.TaskServiceApi;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Profile("h2")
public class TaskServiceH2 implements TaskServiceApi {
    private final TaskJpaRepository repo;

    public TaskServiceH2(TaskJpaRepository repo) { this.repo = repo; }

    private Task toPojo(TaskEntity e) {
        Task t = new Task();
        t.setId(e.getId());
        t.setUserId(e.getUserId());
        t.setTitle(e.getTitle());
        t.setDescription(e.getDescription());
        t.setCreatedAt(e.getCreatedAt());
        t.setDueDate(e.getDueDate());
        t.setCompleted(e.isCompleted());
        t.setDeleted(e.isDeleted());
        return t;
    }

    private TaskEntity toEntity(Task t) {
        TaskEntity e = new TaskEntity();
        e.setId(t.getId());
        e.setUserId(t.getUserId());
        e.setTitle(t.getTitle());
        e.setDescription(t.getDescription());
        e.setCreatedAt(t.getCreatedAt());
        e.setDueDate(t.getDueDate());
        e.setCompleted(t.isCompleted());
        e.setDeleted(t.isDeleted());
        return e;
    }

    @Override
    public Task create(Task t) {
        return toPojo(repo.save(toEntity(t)));
    }

    @Override
    public Optional<Task> get(Long id) {
        return repo.findById(id).filter(e -> !e.isDeleted()).map(this::toPojo);
    }

    @Override
    public List<Task> getAll(Long userId) {
        return repo.findByUserIdAndDeletedFalse(userId).stream().map(this::toPojo).collect(Collectors.toList());
    }

    @Override
    public List<Task> getPending(Long userId) {
        return repo.findByUserIdAndCompletedFalseAndDeletedFalse(userId).stream().map(this::toPojo).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        repo.findById(id).ifPresent(e -> { e.setDeleted(true); repo.save(e); });
    }
}
