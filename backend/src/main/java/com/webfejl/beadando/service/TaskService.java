package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.TaskDTO;
import com.webfejl.beadando.entity.Task;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.TaskRepository;
import com.webfejl.beadando.repository.UserRepository;
import com.webfejl.beadando.util.TaskMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.webfejl.beadando.util.ProjectAccessUtil.checkIfUserIsLoggedIn;
import static com.webfejl.beadando.util.ProjectAccessUtil.getUsername;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final Optional<User> user;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        user = userRepository.findByUsername(getUsername());
    }

    public List<TaskDTO> findAll(String projectId, String status, Integer priority) throws AuthenticationException {
        checkIfUserIsLoggedIn(user);
        return taskRepository.filterTasks(projectId, status, priority)
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO findTask(String id) throws AuthenticationException {
        checkIfUserIsLoggedIn(user);
        return taskRepository.findById(id)
                .map(TaskMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
    }

    public TaskDTO createTask(TaskDTO taskDTO) throws AuthenticationException {
        checkIfUserIsLoggedIn(user);
        Task task = TaskMapper.toEntity(taskDTO, new Task(), projectRepository);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDTO(savedTask);
    }

    public List<TaskDTO> sortTasksByTitle() throws AuthenticationException {
        checkIfUserIsLoggedIn(user);
        return taskRepository.sortByTitle()
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> sortTasksByDate() throws AuthenticationException {
        checkIfUserIsLoggedIn(user);
        return taskRepository.sortByDate()
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO updateTask(TaskDTO taskDTO, String id) throws AuthenticationException {
        checkIfUserIsLoggedIn(user);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));

        Task newTask = TaskMapper.toEntity(taskDTO, task, projectRepository);

        return TaskMapper.toDTO(taskRepository.save(newTask));
    }

    public void deleteTask(String id) throws AuthenticationException {
        checkIfUserIsLoggedIn(user);
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with ID: " + id);
        }
        taskRepository.deleteById(id);
    }
}
