package com.example.todo.services;

import com.example.todo.dto.user.RegisterDTO;
import com.example.todo.dto.user.UserProfileDTO;
import com.example.todo.models.UserModel;
import com.example.todo.repositories.UserRepository;
import com.example.todo.utils.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserProfileDTO findByUsername(String username){
        UserModel userModel = getUserFromUsername(username);
        return mapUserToDTO(userModel);
    }

    public UserProfileDTO create(RegisterDTO registerDTO){
        UserModel userModel = mapRegisterDTOToUserModel(registerDTO);
        userModel = userRepository.save(userModel);
        return mapUserToDTO(userModel);
    }

    public UserProfileDTO update(String username, UserProfileDTO userProfileDTO){
        UserModel userModel = getUserFromUsername(username);
        userModel.setFirstName(userProfileDTO.getFirstName());
        userModel.setLastName(userProfileDTO.getLastName());
        userModel.setEmail(userProfileDTO.getEmail());
        userModel.setUsername(userProfileDTO.getUsername());

        userRepository.save(userModel);
        return userProfileDTO;
    }

    public void delete(String username){
        try {
            userRepository.delete(getUserFromUsername(username));
        }catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private UserModel getUserFromUsername(String username){
        Optional<UserModel> user = Optional.of(userRepository.findUserByUsername(username).orElseThrow(

        ));
        return user.get();
    }

    private UserProfileDTO mapUserToDTO(UserModel userModel){
        return new UserProfileDTO(
                userModel.getId(),
                userModel.getFirstName(),
                userModel.getLastName(),
                userModel.getEmail(),
                userModel.getUsername());
    }

    private UserModel mapRegisterDTOToUserModel(RegisterDTO registerDTO){
        return new UserModel(
                null,
                registerDTO.getFirstName(),
                registerDTO.getLastName(),
                registerDTO.getEmail(),
                registerDTO.getUsername(),
                registerDTO.getPassword(),
                Collections.emptySet(),
                Collections.emptySet());
    }

}
