package com.example.demo.repository.h2;

import com.example.demo.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByUserIdAndDeletedFalse(Long userId);
    List<TaskEntity> findByUserIdAndCompletedFalseAndDeletedFalse(Long userId);
}
