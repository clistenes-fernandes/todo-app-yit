package com.example.todo.controllers;

import com.example.todo.dto.user.UserProfileDTO;
import com.example.todo.security.JWTGenerator;
import com.example.todo.security.SecurityUtils;
import com.example.todo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTGenerator jwtGenerator;

    @GetMapping()
    public ResponseEntity<UserProfileDTO> findByUsername(HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        UserProfileDTO userProfile = userService.findByUsername(username);
        return ResponseEntity.ok().body(userProfile);
    }
    @PutMapping()
    public ResponseEntity<UserProfileDTO> update(@RequestBody UserProfileDTO userProfileDTO,
                                                 HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        UserProfileDTO userProfile = userService.update(username, userProfileDTO);
        return ResponseEntity.ok().body(userProfile);
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(HttpServletRequest request){
        String username = jwtGenerator.getUsernameFromJWT(SecurityUtils.getTokenFromRequest(request));
        userService.delete(username);
        return ResponseEntity.noContent().build();
    }





}
