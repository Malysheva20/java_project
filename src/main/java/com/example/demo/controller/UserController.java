package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.api.UserServiceApi;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceApi service;
    public UserController(UserServiceApi service) { this.service = service; }

    @PostMapping
    public ResponseEntity<User> register(@RequestBody User u) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(u));
    }

    @GetMapping("/login")
    public ResponseEntity<User> login(@RequestParam String username) {
        return service.login(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
