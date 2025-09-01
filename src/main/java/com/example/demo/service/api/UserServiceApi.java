package com.example.demo.service.api;

import com.example.demo.model.User;
import java.util.Optional;

public interface UserServiceApi {
    User register(User u);
    Optional<User> login(String username);
}
