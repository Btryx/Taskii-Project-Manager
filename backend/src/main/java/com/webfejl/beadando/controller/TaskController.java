package com.webfejl.beadando.controller;


import com.webfejl.beadando.dto.AuthenticationResponse;
import com.webfejl.beadando.dto.TaskDTO;
import com.webfejl.beadando.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    @Autowired
    private TaskService service;


    @GetMapping(produces = "application/json")
    @RequestMapping({ "/validateLogin" })
    public AuthenticationResponse validateLogin() {
        return new AuthenticationResponse("User successfully authenticated");
    }

    @GetMapping("/tasks/all")
    public List<TaskDTO> getAllTasks(){
        return service.findAll();
    }

    @GetMapping("/tasks/{id}")
    public TaskDTO getTaskById(@PathVariable String id){
        return service.findTask(id);
    }

    @GetMapping("/tasks/status/{status}")
    public List<TaskDTO> getTasksByStatus(@PathVariable String status) {
        return service.findTasksByStatus(status);
    }

    // Filter by Priority
    @GetMapping("/tasks/priority/{priority}")
    public List<TaskDTO> getTasksByPriority(@PathVariable int priority) {
        return service.findTasksByPriority(priority);
    }

    // Sort by Title
    @GetMapping("/tasks/sort/title")
    public List<TaskDTO> getTasksSortedByTitle() {
        return service.sortTasksByTitle();
    }

    // Sort by Date
    @GetMapping("/tasks/sort/date")
    public List<TaskDTO> getTasksSortedByDate() {
        return service.sortTasksByDate();
    }

    @PostMapping("/tasks/all")
    public void createTask(@RequestBody TaskDTO task){
        service.createTask(task);
    }

    @PutMapping("/tasks/{id}")
    public void updateTask(@PathVariable String id, @RequestBody TaskDTO task){
        service.updateTask(task, id);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable String id){
        service.deleteTask(id);
    }
}
