package com.webfejl.beadando.service;

import com.webfejl.beadando.model.TaskDTO;
import com.webfejl.beadando.repository.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImp implements TaskService {

    @Autowired
    private TaskRepo taskRepo;

    @Override
    public List<TaskDTO> findAll() {
        return taskRepo.getAllTasks();
    }

    @Override
    public TaskDTO findItem(String id) {
        return taskRepo.getTaskById(id);
    }

    @Override
    public int createItem(TaskDTO taskDTO) {
        return taskRepo.createTask(taskDTO);
    }

    @Override
    public int updateItem(TaskDTO taskDTO, String id) {
        return taskRepo.editTask(taskDTO, id);
    }

    @Override
    public int deleteItem(String id) {
        return taskRepo.deleteTask(id);
    }
}
