package com.example.todo.dto.tasklist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskListRequestDTO {
    private String name;
    private String description;
    private List<Long> taskIds;

    public TaskListRequestDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
