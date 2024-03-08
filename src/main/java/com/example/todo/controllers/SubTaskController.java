package com.example.todo.controllers;

import com.example.todo.dto.task.TaskDTO;
import com.example.todo.dto.task.TaskRequestDTO;
import com.example.todo.security.JWTGenerator;
import com.example.todo.security.SecurityUtils;
import com.example.todo.services.SubTaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks/{taskId}/subtasks")
public class SubTaskController {

    @Autowired
    private SubTaskService subTaskService;

    @Autowired
    private JWTGenerator jwtGenerator;

    @GetMapping()
    public ResponseEntity<List<TaskDTO>> findAllSubtasks(@PathVariable Long taskId,
                                                         HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        List<TaskDTO> tasks = subTaskService.findAll(username, taskId);
        return ResponseEntity.ok().body(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> findSubTaskById(@PathVariable Long taskId,
                                                   @PathVariable Long id,
                                                   HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        return ResponseEntity.ok().body(subTaskService.findById(id, username, taskId));
    }


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDTO> createSubTask(@PathVariable Long taskId,
                                              @RequestBody TaskRequestDTO subTaskRequest,
                                              HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        TaskDTO task = subTaskService.create(subTaskRequest, username, taskId);
        return ResponseEntity.ok().body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateSubTask(@PathVariable Long taskId,
                                                 @PathVariable Long id,
                                                 @RequestBody TaskRequestDTO subTaskRequest,
                                                 HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        return ResponseEntity.ok().body(subTaskService.update(id, subTaskRequest, username, taskId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubTask(@PathVariable Long taskId,
                                              @PathVariable Long id,
                                              HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        subTaskService.delete(id, username, taskId);
        return ResponseEntity.noContent().build();
    }
}
