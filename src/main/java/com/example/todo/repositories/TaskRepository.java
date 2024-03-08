package com.example.todo.repositories;

import com.example.todo.models.Task;
import com.example.todo.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByUser(UserModel user, Pageable pageable);
    Optional<Task> findByIdAndUser(Long id, UserModel user);

    List<Task> findByTaskListId(Long task_list_id);
}
