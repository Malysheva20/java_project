package com.example.demo.controller;

import com.example.demo.model.Notification;
import com.example.demo.service.api.NotificationServiceApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationServiceApi service;
    public NotificationController(NotificationServiceApi service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<Notification>> all(@RequestParam Long userId) {
        return ResponseEntity.ok(service.all(userId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Notification>> pending(@RequestParam Long userId) {
        return ResponseEntity.ok(service.pending(userId));
    }
}
