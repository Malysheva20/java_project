package com.example.demo.messaging;

import com.example.demo.entity.NotificationEntity;
import com.example.demo.repository.h2.NotificationJpaRepository; // либо ваш актуальный пакет репозитория
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.demo.config.RabbitConfig.QUEUE_NOTIFICATIONS;

@Component
@Profile("amqp")
public class NotificationEventListener {

    private final NotificationJpaRepository repo;

    public NotificationEventListener(NotificationJpaRepository repo) {
        this.repo = repo;
    }

    @Async
    @RabbitListener(queues = QUEUE_NOTIFICATIONS)
    public void onTaskCreated(TaskCreatedEvent event) {
        NotificationEntity e = new NotificationEntity();
        e.setUserId(event.getUserId());
        e.setMessage("Task created: " + event.getTitle());
        e.setReadFlag(false);
        // setCreatedAt ожидает LocalDateTime
        e.setCreatedAt(LocalDateTime.now());
        repo.save(e);
    }
}
