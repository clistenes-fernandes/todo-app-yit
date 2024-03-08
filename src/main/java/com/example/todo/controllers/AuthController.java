package com.example.todo.controllers;

import com.example.todo.dto.auth.AuthResponseDTO;
import com.example.todo.dto.user.LoginDTO;
import com.example.todo.dto.user.RegisterDTO;
import com.example.todo.models.UserModel;
import com.example.todo.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO){
        UserModel user = authService.register(registerDTO);
        if(user == null){
            return ResponseEntity.badRequest().body("Username already exists");
        }

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body("User Successfully registered");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO){
        AuthResponseDTO authResponseDTO = authService.login(loginDTO);
        return ResponseEntity.ok().body(authResponseDTO);
    }

}
