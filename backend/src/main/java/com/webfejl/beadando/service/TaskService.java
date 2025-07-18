package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.TaskDto;
import com.webfejl.beadando.entity.Comment;
import com.webfejl.beadando.entity.Task;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.TaskNotFoundException;
import com.webfejl.beadando.repository.CommentRepository;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.TaskRepository;
import com.webfejl.beadando.util.AccessUtil;
import com.webfejl.beadando.util.TaskMapper;
import jakarta.transaction.Transactional;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final CommentRepository commentRepository;
    private final AccessUtil accessUtil;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, CommentRepository commentRepository, AccessUtil accessUtil) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.commentRepository = commentRepository;
        this.accessUtil = accessUtil;
    }

    public List<TaskDto> findAll(String projectId, String status, Integer priority) throws AuthenticationException {
        User user = accessUtil.getAuthenticatedUser();
        accessUtil.checkAccess(projectId, user);

        return taskRepository.filterTasks(projectId, status, priority)
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDto findTask(String id) throws AuthenticationException, TaskNotFoundException {
        User user = accessUtil.getAuthenticatedUser();
        TaskDto task =  taskRepository.findById(id)
                .map(TaskMapper::toDTO)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));

        accessUtil.checkAccess(task.projectId(), user);

        return task;
    }

    @Transactional
    public TaskDto createTask(TaskDto taskDTO) throws AuthenticationException {
        User user = accessUtil.getAuthenticatedUser();
        Task task = TaskMapper.toEntity(taskDTO, new Task(), projectRepository);

        accessUtil.checkAccess(task.getProject().getProjectId(), user);

        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDTO(savedTask);
    }

    @Transactional
    public TaskDto updateTask(TaskDto taskDTO, String id) throws AuthenticationException, TaskNotFoundException {
        User user = accessUtil.getAuthenticatedUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));

        accessUtil.checkAccess(task.getProject().getProjectId(), user);

        Task newTask = TaskMapper.toEntity(taskDTO, task, projectRepository);

        return TaskMapper.toDTO(taskRepository.save(newTask));
    }

    @Transactional
    public void deleteTask(String id) throws AuthenticationException, TaskNotFoundException {
        User user = accessUtil.getAuthenticatedUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));

        accessUtil.checkAccess(task.getProject().getProjectId(), user);

        List<Comment> comments = commentRepository.findAllByTask_TaskIdOrderByCreatedAtDesc(id);
        commentRepository.deleteAll(comments);
        taskRepository.deleteById(id);
    }
}
