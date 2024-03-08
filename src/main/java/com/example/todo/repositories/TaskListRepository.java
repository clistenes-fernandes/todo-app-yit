package com.example.todo.repositories;

import com.example.todo.models.TaskList;
import com.example.todo.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    Page<TaskList> findAllByUser(UserModel user, Pageable pageable);

    Optional<TaskList> findByIdAndUser(Long id, UserModel user);
}