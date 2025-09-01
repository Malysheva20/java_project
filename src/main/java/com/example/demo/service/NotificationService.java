package com.example.demo.service;

import com.example.demo.model.Notification;
import com.example.demo.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository repo;
    public NotificationService(NotificationRepository repo) { this.repo = repo; }

    public Notification create(Notification n) { return repo.save(n); }
    public List<Notification> all(Long userId) { return repo.findAllByUser(userId); }
    public List<Notification> pending(Long userId) { return repo.findPendingByUser(userId); }
}
