package com.example.demo.service.pg;

import com.example.demo.entity.NotificationEntity;
import com.example.demo.model.Notification;
import com.example.demo.repository.h2.NotificationJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServicePgTest {

    @Test
    void createMapsAndSaves() {
        NotificationJpaRepository repo = Mockito.mock(NotificationJpaRepository.class);
        NotificationServicePg service = new NotificationServicePg(repo);

        Notification n = new Notification();
        n.setUserId(1L);
        n.setMessage("Hi");

        NotificationEntity saved = new NotificationEntity();
        saved.setId(5L);
        saved.setUserId(1L);
        saved.setMessage("Hi");

        when(repo.save(any(NotificationEntity.class))).thenReturn(saved);
        Notification out = service.create(n);
        assertEquals(5L, out.getId());
        assertEquals("Hi", out.getMessage());
    }

    @Test
    void pendingCallsQuery() {
        NotificationJpaRepository repo = Mockito.mock(NotificationJpaRepository.class);
        NotificationServicePg service = new NotificationServicePg(repo);

        NotificationEntity e = new NotificationEntity();
        e.setId(8L);
        e.setUserId(3L);
        e.setMessage("Ping");

        when(repo.findByUserIdAndReadFlagFalse(3L)).thenReturn(List.of(e));
        List<Notification> list = service.pending(3L);
        assertEquals(1, list.size());
        assertEquals("Ping", list.get(0).getMessage());
    }
}
