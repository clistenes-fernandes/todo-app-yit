package com.example.todo.models;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@MappedSuperclass
public class TaskBase {
    private String title;
    private String description;
    private Instant dueDate;
    private Boolean completed;
}
