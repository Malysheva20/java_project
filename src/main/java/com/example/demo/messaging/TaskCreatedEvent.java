package com.example.demo.messaging;

import java.time.Instant;

public class TaskCreatedEvent {
    private Long taskId;
    private Long userId;
    private String title;
    private Instant createdAt;

    public TaskCreatedEvent() {}

    public TaskCreatedEvent(Long taskId, Long userId, String title, Instant createdAt) {
        this.taskId = taskId;
        this.userId = userId;
        this.title = title;
        this.createdAt = createdAt;
    }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
