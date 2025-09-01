package com.example.demo.repository.misc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OverdueTaskNativeRepository extends JpaRepository<com.example.demo.entity.TaskEntity, Long> {

    @Query(value = """
        SELECT t.id, t.user_id, t.title, t.due_date
        FROM tasks t
        WHERE t.deleted = FALSE
          AND t.due_date IS NOT NULL
          AND t.due_date < ?1
        """, nativeQuery = true)
    List<Object[]> findAllOverdue(LocalDateTime now);
}
