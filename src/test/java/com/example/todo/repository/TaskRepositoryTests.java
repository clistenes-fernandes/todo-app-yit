package com.example.todo.repository;

import com.example.todo.models.Task;
import com.example.todo.models.TaskList;
import com.example.todo.models.UserModel;
import com.example.todo.repositories.TaskListRepository;
import com.example.todo.repositories.TaskRepository;
import com.example.todo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TaskRepositoryTests {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    private UserModel user;

    private TaskList taskList;

    @BeforeEach
    public void populateDatabase(){
        UserModel userModel = new UserModel(null,
                "Clark",
                "Wayne",
                "clark.wayne@email.com",
                "clark.wayne",
                "123456",
                Collections.emptySet(),
                Collections.emptySet()
        );
        user = userRepository.save(userModel);

        taskList = taskListRepository.save(new TaskList(null,
                "Test Task List",
                "Description ot the Test Task List",
                Collections.emptySet(),
                user));

    }

    @Test
    public void TaskRepository_saveTasK_ReturnSavedTask(){
        Task task = new Task(null,
                "Test Task",
                "Description of Test Task",
                Instant.now(), false,
                user);

        Task savedTask = taskRepository.save(task);

        Assertions.assertNotNull(savedTask);
        Assertions.assertTrue(savedTask.getId() > 0);
    }

    @Test
    public void TaskRepository_findAllTasks_ReturnMoreThanOneTask(){
        Task task1 = new Task(null,
                "Test Task",
                "Description of Test Task",
                Instant.now(), false,
                user);

        Task task2 = new Task(null,
                "Test Task",
                "Description of Test Task",
                Instant.now(), false,
                user);

        taskRepository.saveAll(Arrays.asList(task1, task2));

        List<Task> tasks = taskRepository.findAll();

        Assertions.assertNotNull(tasks);
        Assertions.assertFalse(tasks.isEmpty());
        Assertions.assertEquals(2, tasks.size());
    }

    @Test
    public void TaskRepository_findAllByUser_ReturnMoreThanOneTask(){
        Task task1 = new Task(null,
                "Test Task",
                "Description of Test Task",
                Instant.now(), false,
                user);

        Task task2 = new Task(null,
                "Test Task",
                "Description of Test Task",
                Instant.now(), false,
                user);

        taskRepository.saveAll(Arrays.asList(task1, task2));
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> tasksPage = taskRepository.findAllByUser(user, pageable);

        List<Task> tasks = tasksPage.getContent();

        Assertions.assertNotNull(tasks);
        Assertions.assertFalse(tasks.isEmpty());
        Assertions.assertEquals(2, tasks.size());
    }

    @Test
    public void TaskRepository_findByIdAndUser_ReturnNotNull(){
        Task taskSample = new Task(null,
                "Test Task",
                "Description of Test Task",
                Instant.now(), false,
                user);

        Task savedTask = taskRepository.save(taskSample);

        Task task = taskRepository.findByIdAndUser(savedTask.getId(), user).orElseThrow();

        Assertions.assertNotNull(task);
        Assertions.assertSame(task.getId(), savedTask.getId());
    }

    @Test
    public void TaskRepository_findByTaskListId_ReturnMoreThanOne(){
        Task task1 = new Task(null,
                "Test Task",
                "Description of Test Task",
                Instant.now(), false,
                user);

        task1.setTaskList(taskList);

        taskRepository.save(task1);

        List<Task> tasks = taskRepository.findByTaskListId(taskList.getId());

        Assertions.assertNotNull(tasks);
        Assertions.assertFalse(tasks.isEmpty());
        Assertions.assertEquals(1, tasks.size());
    }


    public void TaskRepository_deleteById_ReturnOne(){
        Task task1 = new Task(null,
                "Test Task",
                "Description of Test Task",
                Instant.now(), false,
                user);
        taskRepository.save(task1);
        Task task2 = new Task(null,
                "Test Task",
                "Description of Test Task",
                Instant.now(), false,
                user);
        Task taskToDelete = taskRepository.save(task2);

        taskRepository.deleteById(taskToDelete.getId());

        List<Task> tasks = taskRepository.findAll();

        Assertions.assertNotNull(tasks);
        Assertions.assertFalse(tasks.isEmpty());
        Assertions.assertEquals(1, tasks.size());
    }
}
