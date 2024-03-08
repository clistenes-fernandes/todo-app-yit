package com.example.todo.controllers;

import com.example.todo.dto.task.TaskPageResponseDTO;
import com.example.todo.dto.task.TaskRequestDTO;
import com.example.todo.dto.task.TaskResponseDTO;
import com.example.todo.security.JWTGenerator;
import com.example.todo.security.SecurityUtils;
import com.example.todo.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private JWTGenerator jwtGenerator;

    @GetMapping()
    public ResponseEntity<TaskPageResponseDTO> findAll(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        TaskPageResponseDTO tasks = taskService.findAll(pageNo, pageSize, username);
        return ResponseEntity.ok().body(tasks);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable Long id, HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        TaskResponseDTO taskResponseDTO = taskService.findById(id, username);
        return ResponseEntity.ok().body(taskResponseDTO);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@RequestBody TaskRequestDTO taskRequest, HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        TaskResponseDTO taskResponseDTO = taskService.create(taskRequest, username);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(taskResponseDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(taskResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable Long id,
                                       @RequestBody TaskRequestDTO taskRequest,
                                        HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        TaskResponseDTO taskResponseDTO = taskService.update(id, taskRequest, username);
        return ResponseEntity.ok().body(taskResponseDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(@PathVariable Long id, HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        taskService.delete(id, username);
        return ResponseEntity.noContent().build();
    }
}
