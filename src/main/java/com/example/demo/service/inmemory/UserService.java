package com.example.demo.service.inmemory;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.api.UserServiceApi;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Profile("inmemory")
public class UserService implements UserServiceApi {
    private final UserRepository repo;
    public UserService(UserRepository repo) { this.repo = repo; }

    public User register(User u) { return repo.save(u); }
    public Optional<User> login(String username) { return repo.findByUsername(username); }
}
