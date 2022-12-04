package com.webfejl.beadando.service;

import com.webfejl.beadando.entity.TaskDTO;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<TaskDTO> findAll();

   TaskDTO findItem(String id);

    int createItem(TaskDTO taskDTO);

    int updateItem(TaskDTO taskDTO, String id);

    int deleteItem(String id);
}
