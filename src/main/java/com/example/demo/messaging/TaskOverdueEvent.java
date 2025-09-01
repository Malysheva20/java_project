package com.example.demo.messaging;

import java.time.Instant;

public class TaskOverdueEvent {
    private Long taskId;
    private Long userId;
    private String title;
    private Instant dueAt;

    public TaskOverdueEvent() {}

    public TaskOverdueEvent(Long taskId, Long userId, String title, Instant dueAt) {
        this.taskId = taskId;
        this.userId = userId;
        this.title = title;
        this.dueAt = dueAt;
    }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Instant getDueAt() { return dueAt; }
    public void setDueAt(Instant dueAt) { this.dueAt = dueAt; }
}
