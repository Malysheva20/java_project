package com.example.demo.service.pg;

import com.example.demo.entity.TaskEntity;
import com.example.demo.model.Task;
import com.example.demo.repository.h2.TaskJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServicePgTest {

    @Test
    void createMapsAndSaves() {
        TaskJpaRepository repo = Mockito.mock(TaskJpaRepository.class);
        TaskServicePg service = new TaskServicePg(repo);

        Task t = new Task();
        t.setUserId(1L);
        t.setTitle("Hello");

        TaskEntity saved = new TaskEntity();
        saved.setId(10L);
        saved.setUserId(1L);
        saved.setTitle("Hello");

        when(repo.save(any(TaskEntity.class))).thenReturn(saved);

        Task out = service.create(t);
        assertEquals(10L, out.getId());
        assertEquals("Hello", out.getTitle());
        verify(repo, times(1)).save(any(TaskEntity.class));
    }

    @Test
    void getPendingUsesCustomQuery() {
        TaskJpaRepository repo = Mockito.mock(TaskJpaRepository.class);
        TaskServicePg service = new TaskServicePg(repo);

        TaskEntity e = new TaskEntity();
        e.setId(1L);
        e.setUserId(5L);
        e.setTitle("Task1");
        when(repo.findByUserIdAndCompletedFalseAndDeletedFalse(5L)).thenReturn(List.of(e));

        List<Task> pending = service.getPending(5L);
        assertEquals(1, pending.size());
        assertEquals("Task1", pending.get(0).getTitle());
    }

    @Test
    void deleteIsSoft() {
        TaskJpaRepository repo = Mockito.mock(TaskJpaRepository.class);
        TaskServicePg service = new TaskServicePg(repo);

        TaskEntity e = new TaskEntity();
        e.setId(7L);
        e.setDeleted(false);

        when(repo.findById(7L)).thenReturn(Optional.of(e));
        TaskEntity saved = new TaskEntity();
        saved.setId(7L);
        saved.setDeleted(true);
        when(repo.save(any(TaskEntity.class))).thenReturn(saved);

        service.delete(7L);
        verify(repo, times(1)).save(any(TaskEntity.class));
        assertTrue(saved.isDeleted());
    }
}
