package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService service;

    @BeforeEach
    void setup() {
        service = new UserService(new UserRepository());
    }

    @Test
    void testRegisterAndLogin() {
        User u = new User();
        u.setUsername("alice");
        u.setDisplayName("Alice");

        User created = service.register(u);
        assertNotNull(created.getId());

        User logged = service.login("alice").orElseThrow();
        assertEquals("alice", logged.getUsername());
    }

    @Test
    void testLoginFail() {
        assertTrue(service.login("nonexistent").isEmpty());
    }
}
