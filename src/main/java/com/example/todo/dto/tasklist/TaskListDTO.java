package com.example.todo.dto.tasklist;

import com.example.todo.dto.task.TaskResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskListDTO {
    private Long id;
    private String name;
    private String description;
    private Set<TaskResponseDTO> tasks;
}
