package com.example.todo.services;

import com.example.todo.dto.task.TaskDTO;
import com.example.todo.dto.task.TaskRequestDTO;
import com.example.todo.models.SubTask;
import com.example.todo.models.Task;
import com.example.todo.models.UserModel;
import com.example.todo.repositories.SubTaskRepository;
import com.example.todo.repositories.TaskRepository;
import com.example.todo.repositories.UserRepository;
import com.example.todo.utils.exceptions.DatabaseException;
import com.example.todo.utils.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SubTaskService {

    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public List<TaskDTO> findAll(String username, Long taskId){
        Task task = taskRepository.findByIdAndUser(taskId, getUserFromUsername(username))
                .orElseThrow(() -> new ResourceNotFoundException(taskId));

        List<SubTask> subTasks = subTaskRepository.findByTask(task);
        return subTasks.stream().map(this::mapToDTO).toList();
    }

    public TaskDTO findById(Long id, String username, Long taskId){
        Task task = taskRepository.findByIdAndUser(taskId, getUserFromUsername(username))
                .orElseThrow(() -> new ResourceNotFoundException(id));
        SubTask subTask = subTaskRepository.findByIdAndTask(id, task).orElseThrow(() -> new ResourceNotFoundException(id));
        return mapToDTO(subTask);
    }

    public TaskDTO create(TaskRequestDTO subTaskRequestDTO, String username, Long taskId){
        Task task = taskRepository.findByIdAndUser(taskId, getUserFromUsername(username))
                .orElseThrow(() -> new ResourceNotFoundException(taskId));

        SubTask subTask = mapToSubTask(subTaskRequestDTO);
        subTask.setTask(task);
        subTask = subTaskRepository.save(subTask);

        return mapToDTO(subTask);
    }

    public TaskDTO update(Long id, TaskRequestDTO subTaskRequestDTO, String username, Long taskId){
        try {
            UserModel user = getUserFromUsername(username);

            taskRepository.findByIdAndUser(taskId, user).orElseThrow(() -> new ResourceNotFoundException(id));

            SubTask subTask = subTaskRepository.getReferenceById(id);

            subTask.setTitle(subTaskRequestDTO.getTitle());
            subTask.setDescription(subTaskRequestDTO.getDescription());
            subTask.setDueDate(subTaskRequestDTO.getDueDate());
            subTask.setCompleted(subTaskRequestDTO.getCompleted());

            subTaskRepository.save(subTask);
            return mapToDTO(subTask);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    public void delete(Long id, String username, Long taskId){
        try {
            Task task = taskRepository.findByIdAndUser(taskId, getUserFromUsername(username))
                    .orElseThrow(() -> new ResourceNotFoundException(id));

            SubTask subTask = subTaskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

            if(!Objects.equals(subTask.getTask().getId(), task.getId())) {
                throw new ResourceNotFoundException(id);
            }

            subTaskRepository.delete(subTask);

        }catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private UserModel getUserFromUsername(String username){
        Optional<UserModel> user = Optional.of(userRepository.findUserByUsername(username).orElseThrow());
        return user.get();
    }

    private TaskDTO mapToDTO(SubTask task){
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getCompleted()
        );
    }

    private SubTask mapToSubTask(TaskRequestDTO taskRequestDTO){
        return new SubTask(
                null,
                taskRequestDTO.getTitle(),
                taskRequestDTO.getDescription(),
                taskRequestDTO.getDueDate(),
                taskRequestDTO.getCompleted()
        );
    }


}
