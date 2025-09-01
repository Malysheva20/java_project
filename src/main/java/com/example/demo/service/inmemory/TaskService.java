package com.example.demo.service.inmemory;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.api.TaskServiceApi;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("inmemory")
public class TaskService implements TaskServiceApi {
    private final TaskRepository repo;
    public TaskService(TaskRepository repo) { this.repo = repo; }

    public Task create(Task t) { return repo.save(t); }
    public Optional<Task> get(Long id) { return repo.findById(id); }
    public List<Task> getAll(Long userId) { return repo.findAllByUser(userId); }
    public List<Task> getPending(Long userId) { return repo.findPendingByUser(userId); }
    public void delete(Long id) { repo.softDelete(id); }
}
