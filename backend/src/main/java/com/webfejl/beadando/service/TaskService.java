package com.webfejl.beadando.service;

import java.util.List;

import com.webfejl.beadando.dto.TaskDTO;

public interface TaskService {

    List<TaskDTO> findAll();

    TaskDTO findTask(String id);

    List<TaskDTO> findTasksByStatus(String status);

    List<TaskDTO> findTasksByPriority(int priority);

    List<TaskDTO> sortTasksByTitle();

    List<TaskDTO> sortTasksByDate();

    int createTask(TaskDTO task);

    int updateTask(TaskDTO task, String id);

    int deleteTask(String id);
}
