package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.TaskDTO;
import com.webfejl.beadando.entity.Task;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.exception.TaskNotFoundException;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.TaskRepository;
import com.webfejl.beadando.repository.UserRepository;
import com.webfejl.beadando.util.ProjectAccessUtil;
import com.webfejl.beadando.util.TaskMapper;
import jakarta.transaction.Transactional;
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
    private final ProjectAccessUtil projectAccessUtil;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, ProjectAccessUtil projectAccessUtil) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.projectAccessUtil = projectAccessUtil;
    }

    public List<TaskDTO> findAll(String projectId, String status, Integer priority) throws AuthenticationException {
        User user = projectAccessUtil.getAuthenticatedUser();
        projectAccessUtil.checkAccess(projectId, user);

        return taskRepository.filterTasks(projectId, status, priority)
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO findTask(String id) throws AuthenticationException, TaskNotFoundException {
        User user = projectAccessUtil.getAuthenticatedUser();
        TaskDTO task =  taskRepository.findById(id)
                .map(TaskMapper::toDTO)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));

        projectAccessUtil.checkAccess(task.projectId(), user);

        return task;
    }

    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) throws AuthenticationException {
        User user = projectAccessUtil.getAuthenticatedUser();
        Task task = TaskMapper.toEntity(taskDTO, new Task(), projectRepository);

        projectAccessUtil.checkAccess(task.getProject().getProjectId(), user);

        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDTO(savedTask);
    }

    @Transactional
    public TaskDTO updateTask(TaskDTO taskDTO, String id) throws AuthenticationException, TaskNotFoundException {
        User user = projectAccessUtil.getAuthenticatedUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));

        projectAccessUtil.checkAccess(task.getProject().getProjectId(), user);

        Task newTask = TaskMapper.toEntity(taskDTO, task, projectRepository);

        return TaskMapper.toDTO(taskRepository.save(newTask));
    }

    @Transactional
    public void deleteTask(String id) throws AuthenticationException, TaskNotFoundException {
        User user = projectAccessUtil.getAuthenticatedUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));

        projectAccessUtil.checkAccess(task.getProject().getProjectId(), user);

        taskRepository.deleteById(id);
    }
}
