package com.webfejl.beadando.controller;

import com.webfejl.beadando.entity.TaskDTO;
import com.webfejl.beadando.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    @Autowired
    private TaskService service;


    @GetMapping("/tasks/all")
    public List<TaskDTO> selectAllTasks(){
        return service.findAll();
    }

    @GetMapping("/tasks/{id}")
    public TaskDTO selectTaskById(@PathVariable String id){
        return service.findItem(id);
    }

    @PostMapping("/tasks/all")
    public void createTask(@RequestBody TaskDTO task){
        service.createItem(task);
    }

    @PutMapping("/tasks/{id}")
    public void updateTask(@PathVariable String id, @RequestBody TaskDTO task){
        service.updateItem(task, id);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable String id){
        service.deleteItem(id);
    }
}
