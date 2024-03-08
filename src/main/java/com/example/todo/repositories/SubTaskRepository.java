package com.example.todo.repositories;

import com.example.todo.models.SubTask;
import com.example.todo.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
    List<SubTask> findByTask(Task task);

    Optional<SubTask> findByIdAndTask(Long id, Task task);
}
