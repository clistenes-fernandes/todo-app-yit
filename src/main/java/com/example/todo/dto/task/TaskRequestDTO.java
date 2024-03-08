package com.example.todo.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskRequestDTO {
    private String title;
    private String description;
    private Instant dueDate;
    private Boolean completed;
    private Long listId;

    public TaskRequestDTO(String title, String description, Instant dueDate, Boolean completed) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
    }
}
