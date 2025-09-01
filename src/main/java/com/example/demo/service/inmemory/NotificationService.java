package com.example.demo.service.inmemory;

import com.example.demo.model.Notification;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.api.NotificationServiceApi;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("inmemory")
public class NotificationService implements NotificationServiceApi {
    private final NotificationRepository repo;
    public NotificationService(NotificationRepository repo) { this.repo = repo; }

    public Notification create(Notification n) { return repo.save(n); }
    public List<Notification> all(Long userId) { return repo.findAllByUser(userId); }
    public List<Notification> pending(Long userId) { return repo.findPendingByUser(userId); }
}
