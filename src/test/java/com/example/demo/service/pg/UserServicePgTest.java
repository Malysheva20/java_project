package com.example.demo.service.pg;

import com.example.demo.entity.UserEntity;
import com.example.demo.model.User;
import com.example.demo.repository.h2.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServicePgTest {

    @Test
    void registerMapsAndSaves() {
        UserJpaRepository repo = Mockito.mock(UserJpaRepository.class);
        UserServicePg service = new UserServicePg(repo);

        User u = new User();
        u.setUsername("alice");

        UserEntity saved = new UserEntity();
        saved.setId(1L);
        saved.setUsername("alice");

        when(repo.save(any(UserEntity.class))).thenReturn(saved);
        User out = service.register(u);
        assertEquals(1L, out.getId());
        assertEquals("alice", out.getUsername());
    }

    @Test
    void loginMapsFromRepo() {
        UserJpaRepository repo = Mockito.mock(UserJpaRepository.class);
        UserServicePg service = new UserServicePg(repo);

        UserEntity found = new UserEntity();
        found.setId(2L);
        found.setUsername("bob");

        when(repo.findByUsername("bob")).thenReturn(Optional.of(found));
        assertTrue(service.login("bob").isPresent());
        assertEquals(2L, service.login("bob").get().getId());
    }
}
