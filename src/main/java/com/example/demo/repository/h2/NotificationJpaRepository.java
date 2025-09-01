package com.example.demo.repository.h2;

import com.example.demo.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUserId(Long userId);
    List<NotificationEntity> findByUserIdAndReadFlagFalse(Long userId);
}
