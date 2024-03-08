package com.example.todo.config;

import com.example.todo.models.Task;
import com.example.todo.models.TaskList;
import com.example.todo.models.UserModel;
import com.example.todo.repositories.TaskListRepository;
import com.example.todo.repositories.TaskRepository;
import com.example.todo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Collections;

/*
* Just to insert initial values in the database
* */

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception{
        UserModel userModel1 = new UserModel(null,
                "John",
                "Smith",
                "john.smith@email.com",
                "john.smith",
                passwordEncoder.encode("2342432424324243234"),
                Collections.emptySet(),
                Collections.emptySet());

        UserModel userInstance = userRepository.save(userModel1);


        Task task1 = new Task(1L, "Update Application Menu",
                "Update the size of the menu tab",
                Instant.now(),
                false,
                userInstance);

        taskRepository.save(task1);

        TaskList taskList1 = new TaskList(null,
                "List 1",
                "description list 1",
                Collections.emptySet(),
                userModel1
                );

        taskListRepository.save(taskList1);
    }
}
