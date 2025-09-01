package com.example.demo.service.pg;

import com.example.demo.entity.NotificationEntity;
import com.example.demo.model.Notification;
import com.example.demo.repository.h2.NotificationJpaRepository;
import com.example.demo.service.api.NotificationServiceApi;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("pg")
public class NotificationServicePg implements NotificationServiceApi {
    private final NotificationJpaRepository repo;

    public NotificationServicePg(NotificationJpaRepository repo) { this.repo = repo; }

    private Notification toPojo(NotificationEntity e) {
        Notification n = new Notification();
        n.setId(e.getId());
        n.setUserId(e.getUserId());
        n.setMessage(e.getMessage());
        n.setRead(e.isReadFlag());
        n.setCreatedAt(e.getCreatedAt());
        return n;
    }

    private NotificationEntity toEntity(Notification n) {
        NotificationEntity e = new NotificationEntity();
        e.setId(n.getId());
        e.setUserId(n.getUserId());
        e.setMessage(n.getMessage());
        e.setReadFlag(n.isRead());
        e.setCreatedAt(n.getCreatedAt());
        return e;
    }

    @Override
    public Notification create(Notification n) {
        return toPojo(repo.save(toEntity(n)));
    }

    @Override
    public List<Notification> all(Long userId) {
        return repo.findByUserId(userId).stream().map(this::toPojo).collect(Collectors.toList());
    }

    @Override
    public List<Notification> pending(Long userId) {
        return repo.findByUserIdAndReadFlagFalse(userId).stream().map(this::toPojo).collect(Collectors.toList());
    }
}
