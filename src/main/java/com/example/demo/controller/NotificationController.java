package com.example.demo.controller;

import com.example.demo.model.Notification;
import com.example.demo.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService service;
    public NotificationController(NotificationService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<Notification>> all(@RequestParam Long userId) {
        return ResponseEntity.ok(service.all(userId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Notification>> pending(@RequestParam Long userId) {
        return ResponseEntity.ok(service.pending(userId));
    }
}
