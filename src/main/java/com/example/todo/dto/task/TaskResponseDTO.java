package com.example.todo.dto.task;

import com.example.todo.models.SubTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Instant dueDate;
    private Boolean completed;
    private Set<SubTask> subTasks;
}
