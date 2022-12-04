package com.webfejl.beadando.service;

import com.webfejl.beadando.model.TaskDTO;

import java.util.List;

public interface TaskService {

    List<TaskDTO> findAll();

   TaskDTO findItem(String id);

    int createItem(TaskDTO taskDTO);

    int updateItem(TaskDTO taskDTO, String id);

    int deleteItem(String id);
}
