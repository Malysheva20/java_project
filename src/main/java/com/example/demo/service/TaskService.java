package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository repo;
    public TaskService(TaskRepository repo) { this.repo = repo; }

    public Task create(Task t) { return repo.save(t); }
    public Optional<Task> get(Long id) { return repo.findById(id); }
    public List<Task> getAll(Long userId) { return repo.findAllByUser(userId); }
    public List<Task> getPending(Long userId) { return repo.findPendingByUser(userId); }
    public void delete(Long id) { repo.softDelete(id); }
}
