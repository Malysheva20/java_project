package com.example.demo.service.api;

import com.example.demo.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskServiceApi {
    Task create(Task t);
    Optional<Task> get(Long id);
    List<Task> getAll(Long userId);
    List<Task> getPending(Long userId);
    void delete(Long id);
}
