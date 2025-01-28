package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.TaskDTO;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.Task;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.TaskRepository;
import com.webfejl.beadando.util.TaskMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public List<TaskDTO> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO findTask(String id) {
        return taskRepository.findById(id)
                .map(TaskMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = TaskMapper.toEntity(taskDTO, new Task(), projectRepository);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDTO(savedTask);
    }

    public List<TaskDTO> findTasksByStatus(String status) {
        return taskRepository.findByStatus(status)
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> findTasksByPriority(int priority) {
        return taskRepository.findByPriority(priority)
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> sortTasksByTitle() {
        return taskRepository.sortByTitle()
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> sortTasksByDate() {
        return taskRepository.sortByDate()
                .stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO updateTask(TaskDTO taskDTO, String id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));

        Task newTask = TaskMapper.toEntity(taskDTO, task, projectRepository);

        return TaskMapper.toDTO(taskRepository.save(newTask));
    }

    public void deleteTask(String id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with ID: " + id);
        }
        taskRepository.deleteById(id);
    }
}
