package com.example.demo.scheduler;

import com.example.demo.messaging.TaskOverdueEvent;
import com.example.demo.repository.h2.NotificationJpaRepository;
import com.example.demo.repository.misc.OverdueTaskNativeRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.example.demo.config.RabbitConfig.EXCHANGE;

@Component
@Profile("sched")
public class OverdueTaskScheduler {

    private final OverdueTaskNativeRepository overdueRepo;
    private final NotificationJpaRepository notificationRepo;
    private final RabbitTemplate rabbitTemplate; // может быть null, если профиль amqp не активен

    @Autowired
    public OverdueTaskScheduler(
            OverdueTaskNativeRepository overdueRepo,
            NotificationJpaRepository notificationRepo,
            @Autowired(required = false) RabbitTemplate rabbitTemplate
    ) {
        this.overdueRepo = overdueRepo;
        this.notificationRepo = notificationRepo;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelayString = "${app.scheduling.overdue.fixedDelay:60000}")
    public void checkOverdue() {
        processOverdue();
    }

    @Async
    protected void processOverdue() {
        LocalDateTime now = LocalDateTime.now();
        List<Object[]> rows = overdueRepo.findAllOverdue(now);
        for (Object[] r : rows) {
            Long taskId   = ((Number) r[0]).longValue();
            Long userId   = ((Number) r[1]).longValue();
            String title  = (String) r[2];
            LocalDateTime target = (LocalDateTime) r[3];

            if (rabbitTemplate != null) {
                Instant dueAt = target.atZone(ZoneId.systemDefault()).toInstant();
                TaskOverdueEvent evt = new TaskOverdueEvent(taskId, userId, title, dueAt);
                rabbitTemplate.convertAndSend(EXCHANGE, "task.overdue", evt);
            } else {
                // фолбэк — создаём уведомление напрямую
                com.example.demo.entity.NotificationEntity e = new com.example.demo.entity.NotificationEntity();
                e.setUserId(userId);
                e.setMessage("Task overdue: " + title);
                e.setReadFlag(false);
                e.setCreatedAt(LocalDateTime.now());
                notificationRepo.save(e);
            }
        }
    }
}
