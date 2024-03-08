package com.example.todo.services;

import com.example.todo.dto.task.TaskResponseDTO;
import com.example.todo.dto.tasklist.TaskListDTO;
import com.example.todo.dto.tasklist.TaskListPageResponseDTO;
import com.example.todo.dto.tasklist.TaskListRequestDTO;
import com.example.todo.models.SubTask;
import com.example.todo.models.Task;
import com.example.todo.models.TaskList;
import com.example.todo.models.UserModel;
import com.example.todo.repositories.SubTaskRepository;
import com.example.todo.repositories.TaskListRepository;
import com.example.todo.repositories.TaskRepository;
import com.example.todo.repositories.UserRepository;
import com.example.todo.utils.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskListService {


    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private UserRepository userRepository;

    public TaskListPageResponseDTO findAll(int pageNo, int pageSize,String username){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        UserModel userModel = getUserFromUsername(username);
        Page<TaskList> taskListsPage = taskListRepository.findAllByUser(userModel, pageable);

        List<TaskListDTO> taskListResponses = new ArrayList<>();

        for(TaskList taskList : taskListsPage.getContent()){
            TaskListDTO taskListDTO = createTaskListResponse(taskList);
            taskListResponses.add(taskListDTO);
        }
        return new TaskListPageResponseDTO(
                taskListResponses,
                taskListsPage.getNumber(),
                taskListsPage.getSize(),
                taskListsPage.getTotalElements(),
                taskListsPage.getTotalPages(),
                taskListsPage.isLast()
        );

    }

    private TaskListDTO findById(Long taskListId, String username){
        TaskList taskList = taskListRepository.findByIdAndUser(taskListId, getUserFromUsername(username))
                .orElseThrow(() -> new ResourceNotFoundException(taskListId));
        return createTaskListResponse(taskList);
    }

    public TaskListDTO create(TaskListRequestDTO taskListRequestDTO, String username){
        UserModel user = getUserFromUsername(username);
        TaskList taskList = mapDtoToModel(taskListRequestDTO, user);

        taskList = taskListRepository.save(taskList);

        Set<Task> tasks = new HashSet<>();

        for(Task t :  getTasks(taskListRequestDTO.getTaskIds())){
            t.setTaskList(taskList);
            tasks.add(t);
        }
        taskRepository.saveAll(tasks);

        return createTaskListResponse(taskList);
    }

    public TaskListDTO update(Long taskListId, TaskListRequestDTO taskListRequestDTO, String username){
        TaskList taskList = taskListRepository.getReferenceById(taskListId);
        for(Task t : taskList.getTasks()) {
            System.out.println(t);
        }
        taskList.setName(taskListRequestDTO.getName());
        taskList.setDescription(taskList.getDescription());

        taskList = taskListRepository.save(taskList);

        saveTasksInList(taskListRequestDTO.getTaskIds(), taskList);
        return createTaskListResponse(taskList);
    }


    public void delete(Long taskListId, String username){

        TaskList taskList = taskListRepository.findByIdAndUser(taskListId, getUserFromUsername(username))
                .orElseThrow(() -> new ResourceNotFoundException(taskListId));
        makeTasksListIdNull(taskList);
        taskListRepository.deleteById(taskListId);
    }


    private UserModel getUserFromUsername(String username){
        Optional<UserModel> user = Optional.of(userRepository.findUserByUsername(username).orElseThrow());
        return user.get();
    }


    private TaskList mapDtoToModel(TaskListRequestDTO taskListRequestDTO, UserModel user){
        return new TaskList(null,
                taskListRequestDTO.getName(),
                taskListRequestDTO.getDescription(),
                Collections.emptySet(),
                user);
    }

    private void saveTasksInList(List<Long> taskIds, TaskList taskList){
        Set<Task> tasks = new HashSet<>();

        for(Task t :  getTasks(taskIds)){
            t.setTaskList(null);
            tasks.add(t);
        }
        taskRepository.saveAll(tasks);
    }

    private List<Task> getTasks(List<Long> taskIds){
        return taskRepository.findAllById(taskIds);
    }

    private void makeTasksListIdNull(TaskList taskList){
        List<Task> tasks = taskRepository.findByTaskListId(taskList.getId());
        for(Task t : tasks){
            t.setTaskList(null);
        }
    }


    private TaskListDTO createTaskListResponse(TaskList taskList){
        List<Task> tasks = taskRepository.findByTaskListId(taskList.getId());
        Set<TaskResponseDTO> taskResponseDTOS = new HashSet<>();

        for(Task task : tasks){
            taskResponseDTOS.add(createTaskResponseDTO(task));
        }
        return new TaskListDTO(taskList.getId(),
                taskList.getName(),
                taskList.getDescription(),
                taskResponseDTOS);
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
