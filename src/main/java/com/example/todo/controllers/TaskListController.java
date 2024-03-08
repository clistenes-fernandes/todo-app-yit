package com.example.todo.controllers;

import com.example.todo.dto.tasklist.TaskListDTO;
import com.example.todo.dto.tasklist.TaskListPageResponseDTO;
import com.example.todo.dto.tasklist.TaskListRequestDTO;
import com.example.todo.security.JWTGenerator;
import com.example.todo.security.SecurityUtils;
import com.example.todo.services.TaskListService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lists")
public class TaskListController {

    @Autowired
    private TaskListService taskListService;

    @Autowired
    private JWTGenerator jwtGenerator;

    @GetMapping()
    public ResponseEntity<TaskListPageResponseDTO> findAll(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        TaskListPageResponseDTO taskListPageResponseDTO = taskListService.findAll(pageNo, pageSize, username);
        return ResponseEntity.ok().body(taskListPageResponseDTO);
    }

    @PostMapping()
    public ResponseEntity<TaskListDTO> create(@RequestBody TaskListRequestDTO taskListRequestDTO, HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        TaskListDTO taskList = taskListService.create(taskListRequestDTO, username);
        return ResponseEntity.ok().body(taskList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskListDTO> update(@PathVariable Long id,
                                              @RequestBody TaskListRequestDTO taskListRequestDTO,
                                              HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        TaskListDTO taskList = taskListService.update(id, taskListRequestDTO, username);
        return ResponseEntity.ok().body(taskList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        taskListService.delete(id, username);
        return ResponseEntity.noContent().build();
    }
}
