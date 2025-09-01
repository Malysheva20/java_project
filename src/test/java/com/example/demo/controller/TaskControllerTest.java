package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.inmemory.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;
    private TaskService service;

    @BeforeEach
    void setup() {
        service = new TaskService(new TaskRepository());
        mockMvc = MockMvcBuilders.standaloneSetup(new TaskController(service)).build();
    }

    @Test
    void testCreateAndGet() throws Exception {
        String json = "{\"userId\":1,\"title\":\"Test Task\",\"dueDate\":\"2025-09-05T12:00:00\"}";
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"));

        mockMvc.perform(get("/tasks").param("userId","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    void testDelete() throws Exception {
        Task t = new Task();
        t.setUserId(1L);
        t.setTitle("DeleteMe");
        service.create(t);

        mockMvc.perform(delete("/tasks/" + t.getId()))
                .andExpect(status().isNoContent());
    }
}
