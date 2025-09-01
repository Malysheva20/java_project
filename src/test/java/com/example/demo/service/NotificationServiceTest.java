package com.example.demo.service;

import com.example.demo.model.Notification;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.api.NotificationServiceApi;
import com.example.demo.service.inmemory.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    private NotificationServiceApi service;

    @BeforeEach
    void setup() {
        service = new NotificationService(new NotificationRepository());
    }

    @Test
    void testCreateAndFetch() {
        Notification n = new Notification();
        n.setUserId(1L);
        n.setMessage("Hello");

        Notification created = service.create(n);
        assertNotNull(created.getId());

        List<Notification> all = service.all(1L);
        assertEquals(1, all.size());

        List<Notification> pending = service.pending(1L);
        assertEquals(1, pending.size());
    }

    @Test
    void testMarkRead() {
        Notification n = new Notification();
        n.setUserId(1L);
        n.setMessage("Read me");
        Notification created = service.create(n);
        created.setRead(true);

        List<Notification> pending = service.pending(1L);
        assertEquals(0, pending.size());
    }
}
