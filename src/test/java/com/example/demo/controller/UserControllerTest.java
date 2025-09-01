package com.example.demo.controller;

import com.example.demo.repository.UserRepository;
import com.example.demo.service.inmemory.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        var service = new UserService(new UserRepository());
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(service)).build();
    }

    @Test
    void testRegister() throws Exception {
        String json = "{\"username\":\"alice\",\"displayName\":\"Alice\"}";
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("alice"));
    }

    @Test
    void testLoginFail() throws Exception {
        mockMvc.perform(get("/users/login")
                        .param("username","bob"))
                .andExpect(status().isNotFound());
    }
}
