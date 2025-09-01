package com.example.demo.messaging;

import com.example.demo.config.RabbitConfig;
import com.example.demo.model.Task;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Aspect
@Component
@Profile("amqp")
public class TaskCreatePublishAspect {

    private final RabbitTemplate rabbitTemplate;

    public TaskCreatePublishAspect(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @AfterReturning(
        pointcut = "execution(* com.example.demo.service..TaskServiceApi.create(..)) && args(task)",
        returning = "result"
    )
    public void publishTaskCreated(Object result, Object task) {
        if (result instanceof Task && task instanceof Task) {
            Task saved = (Task) result;

            // saved.getCreatedAt() = LocalDateTime -> конвертируем в Instant
            Instant createdAtInstant;
            LocalDateTime ldt = saved.getCreatedAt(); // предположительно LocalDateTime
            if (ldt != null) {
                createdAtInstant = ldt.atZone(ZoneId.systemDefault()).toInstant();
            } else {
                createdAtInstant = Instant.now();
            }

            TaskCreatedEvent evt = new TaskCreatedEvent(
                    saved.getId(),
                    saved.getUserId(),
                    saved.getTitle(),
                    createdAtInstant
            );
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_TASK_CREATED, evt);
        }
    }
}
