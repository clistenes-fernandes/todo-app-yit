package com.example.todo.services;

import com.example.todo.dto.auth.AuthResponseDTO;
import com.example.todo.dto.user.LoginDTO;
import com.example.todo.dto.user.RegisterDTO;
import com.example.todo.models.UserModel;
import com.example.todo.repositories.UserRepository;
import com.example.todo.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserModel register(RegisterDTO registerDTO){
        if(userRepository.existsByUsername(registerDTO.getUsername()) ||
                userRepository.existsByEmail(registerDTO.getEmail())){
            return null;
        }
        UserModel user = new UserModel(null,
                registerDTO.getFirstName(),
                registerDTO.getLastName(),
                registerDTO.getEmail(),
                registerDTO.getUsername(),
                passwordEncoder.encode(registerDTO.getPassword()),
                Collections.emptySet(),
                Collections.emptySet());

        return userRepository.save(user);
    }

    public AuthResponseDTO login(LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        return new AuthResponseDTO(token);
    }


}
