package com.example.todo.services;

import com.example.todo.dto.task.TaskPageResponseDTO;
import com.example.todo.dto.task.TaskRequestDTO;
import com.example.todo.dto.task.TaskResponseDTO;
import com.example.todo.models.SubTask;
import com.example.todo.models.Task;
import com.example.todo.models.TaskList;
import com.example.todo.models.UserModel;
import com.example.todo.repositories.SubTaskRepository;
import com.example.todo.repositories.TaskListRepository;
import com.example.todo.repositories.TaskRepository;
import com.example.todo.repositories.UserRepository;
import com.example.todo.utils.exceptions.DatabaseException;
import com.example.todo.utils.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private SubTaskRepository subTaskRepository;

    public TaskPageResponseDTO findAll(int pageNo, int pageSize, String username){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Task> tasksPage = taskRepository.findAllByUser(getUserFromUsername(username), pageable);

        List<TaskResponseDTO> taskResponseDTOS = new ArrayList<>();

        for(Task task : tasksPage.getContent()){
            TaskResponseDTO taskResponseDTO = createTaskResponseDTO(task);
            taskResponseDTOS.add(taskResponseDTO);
        }
        return new TaskPageResponseDTO(
                taskResponseDTOS,
                tasksPage.getNumber(),
                tasksPage.getSize(),
                tasksPage.getTotalElements(),
                tasksPage.getTotalPages(),
                tasksPage.isLast()
        );
    }

    public TaskResponseDTO findById(Long id, String username){
        Task task = taskRepository.findByIdAndUser(id, getUserFromUsername(username))
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return createTaskResponseDTO(task);
    }


    public TaskResponseDTO create(TaskRequestDTO taskRequestDTO, String username){
        UserModel userModel = getUserFromUsername(username);
        Task task = mapToTask(taskRequestDTO, userModel);
        if (taskRequestDTO.getListId() != null){
            TaskList taskList = findTaskListById(taskRequestDTO.getListId(), userModel);
            task.setTaskList(taskList);
        }
        task = taskRepository.save(task);
        return createTaskResponseDTO(task);
    }

    public TaskResponseDTO update(Long id, TaskRequestDTO taskRequestDTO, String username){
        try {
            Task task = taskRepository.findByIdAndUser(id, getUserFromUsername(username))
                    .orElseThrow(() -> new ResourceNotFoundException(id));

            task.setTitle(taskRequestDTO.getTitle());
            task.setDescription(taskRequestDTO.getDescription());
            task.setDueDate(taskRequestDTO.getDueDate());
            task.setCompleted(taskRequestDTO.getCompleted());

            if (Boolean.TRUE.equals(taskRequestDTO.getCompleted())) {
                completeAllSubtasks(task);
            }
            taskRepository.save(task);
            return createTaskResponseDTO(task);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    public void delete(Long id, String username){
        try {
            Task task = taskRepository.findByIdAndUser(id, getUserFromUsername(username)).orElseThrow(
                    () -> new ResourceNotFoundException(id));
            taskRepository.delete(task);
        }catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private UserModel getUserFromUsername(String username){
        Optional<UserModel> user = Optional.of(userRepository.findUserByUsername(username).orElseThrow());
        return user.get();
    }

    private void completeAllSubtasks(Task task){
        List<SubTask> subTasks = subTaskRepository.findByTask(task);
        subTasks.forEach(subTask -> subTask.setCompleted(true));
        subTaskRepository.saveAll(subTasks);
    }

    private Task mapToTask(TaskRequestDTO taskRequestDTO, UserModel user){
        return new Task(
                null,
                taskRequestDTO.getTitle(),
                taskRequestDTO.getDescription(),
                taskRequestDTO.getDueDate(),
                taskRequestDTO.getCompleted(),
                user
        );
    }

    private TaskList findTaskListById(Long id, UserModel user){
        return taskListRepository.findByIdAndUser(id, user).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    private TaskResponseDTO createTaskResponseDTO(Task task){
        List<SubTask> subTasks = subTaskRepository.findByTask(task);
        return new TaskResponseDTO(task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getCompleted(),
                new HashSet<>(subTasks)
                );
    }


}
