package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.TaskDTO;
import com.webfejl.beadando.repository.TaskRepository;
import com.webfejl.beadando.util.TaskMapper;
import com.webfejl.beadando.entity.Task;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImp implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImp(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<TaskDTO> findAll() {
        return taskRepository.getAllTasks()
        .stream()
        .map(TaskMapper::toDTO)
        .collect(Collectors.toList());
    }

    @Override
    public TaskDTO findTask(String id) {
        return TaskMapper.toDTO(taskRepository.getTaskById(id));
    }

    @Override
    public int createTask(TaskDTO taskDTO) {
        Task task = TaskMapper.toEntity(taskDTO);
        return taskRepository.createTask(task);
    }

    @Override
    public List<TaskDTO> findTasksByStatus(String status) {
        List<Task> tasks = taskRepository.findByStatus(status);
        return tasks.stream().map(TaskMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> findTasksByPriority(int priority) {
        List<Task> tasks = taskRepository.findByPriority(priority);
        return tasks.stream().map(TaskMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> sortTasksByTitle() {
        List<Task> tasks = taskRepository.sortByTitle();
        return tasks.stream().map(TaskMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> sortTasksByDate() {
        List<Task> tasks = taskRepository.sortByDate();
        return tasks.stream().map(TaskMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public int updateTask(TaskDTO taskDTO, String id) {
        Task task = TaskMapper.toEntity(taskDTO);
        return taskRepository.editTask(task, id);
    }

    @Override
    public int deleteTask(String id) {
        return taskRepository.deleteTask(id);
    }
}
