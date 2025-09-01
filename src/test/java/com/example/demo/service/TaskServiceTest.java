package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService service;

    @BeforeEach
    void setup() {
        service = new TaskService(new TaskRepository());
    }

    @Test
    void testCreateAndGetTask() {
        Task t = new Task();
        t.setUserId(1L);
        t.setTitle("Test Task");
        t.setDueDate(LocalDateTime.now().plusDays(1));
        Task created = service.create(t);

        assertNotNull(created.getId());
        assertEquals("Test Task", created.getTitle());

        Task fetched = service.get(created.getId()).orElseThrow();
        assertEquals(created.getId(), fetched.getId());
    }

    @Test
    void testPendingTasks() {
        Task t1 = new Task();
        t1.setUserId(1L);
        t1.setTitle("Task 1");
        t1.setCompleted(false);
        service.create(t1);

        Task t2 = new Task();
        t2.setUserId(1L);
        t2.setTitle("Task 2");
        t2.setCompleted(true);
        service.create(t2);

        List<Task> pending = service.getPending(1L);
        assertEquals(1, pending.size());
        assertFalse(pending.get(0).isCompleted());
    }

    @Test
    void testSoftDelete() {
        Task t = new Task();
        t.setUserId(1L);
        t.setTitle("Delete Me");
        Task created = service.create(t);

        service.delete(created.getId());
        assertTrue(service.get(created.getId()).isEmpty());
    }
}
