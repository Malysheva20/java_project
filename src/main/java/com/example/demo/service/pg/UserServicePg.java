package com.example.demo.service.pg;

import com.example.demo.entity.UserEntity;
import com.example.demo.model.User;
import com.example.demo.repository.h2.UserJpaRepository;
import com.example.demo.service.api.UserServiceApi;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Profile("pg")
public class UserServicePg implements UserServiceApi {
    private final UserJpaRepository repo;

    public UserServicePg(UserJpaRepository repo) { this.repo = repo; }

    private User toPojo(UserEntity e) {
        User u = new User();
        u.setId(e.getId());
        u.setUsername(e.getUsername());
        u.setDisplayName(e.getDisplayName());
        return u;
    }

    private UserEntity toEntity(User u) {
        UserEntity e = new UserEntity();
        e.setId(u.getId());
        e.setUsername(u.getUsername());
        e.setDisplayName(u.getDisplayName());
        return e;
    }

    @Override
    public User register(User u) {
        return toPojo(repo.save(toEntity(u)));
    }

    @Override
    public Optional<User> login(String username) {
        return repo.findByUsername(username).map(this::toPojo);
    }
}
