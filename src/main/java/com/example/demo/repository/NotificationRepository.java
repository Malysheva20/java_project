package com.example.demo.repository;

import com.example.demo.model.Notification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class NotificationRepository {
    private final Map<Long, Notification> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public Notification save(Notification n) {
        if (n.getId() == null) n.setId(idGen.getAndIncrement());
        if (n.getCreatedAt() == null) n.setCreatedAt(LocalDateTime.now());
        store.put(n.getId(), n);
        return n;
    }

    public List<Notification> findAllByUser(Long userId) {
        return store.values().stream()
                .filter(n -> Objects.equals(n.getUserId(), userId))
                .toList();
    }

    public List<Notification> findPendingByUser(Long userId) {
        return store.values().stream()
                .filter(n -> Objects.equals(n.getUserId(), userId) && !n.isRead())
                .toList();
    }
}
