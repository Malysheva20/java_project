package com.example.demo.service.api;

import com.example.demo.model.Notification;
import java.util.List;

public interface NotificationServiceApi {
    Notification create(Notification n);
    List<Notification> all(Long userId);
    List<Notification> pending(Long userId);
}
