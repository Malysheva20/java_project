package com.example.demo.controller;

import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.inmemory.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NotificationControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        var service = new NotificationService(new NotificationRepository());
        mockMvc = MockMvcBuilders.standaloneSetup(new NotificationController(service)).build();
    }

    @Test
    void testAllAndPending() throws Exception {
        mockMvc.perform(get("/notifications").param("userId","1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/notifications/pending").param("userId","1"))
                .andExpect(status().isOk());
    }
}
