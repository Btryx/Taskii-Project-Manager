package com.webfejl.beadando.controller;


import com.webfejl.beadando.dto.TaskDTO;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TaskDTO>> filterTasks(
            @RequestParam String projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer priority
    ) {
        try {
            return ResponseEntity.ok(taskService.findAll(projectId, status, priority));
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(taskService.findTask(id));
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/sort/title")
    public ResponseEntity<List<TaskDTO>> getTasksSortedByTitle() {
        try {
            return ResponseEntity.ok(taskService.sortTasksByTitle());
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/sort/date")
    public ResponseEntity<List<TaskDTO>> getTasksSortedByDate() {
        try {
            return ResponseEntity.ok(taskService.sortTasksByDate());
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/all")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO task) {
        try {
            return ResponseEntity.ok(taskService.createTask(task));
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable String id,
            @RequestBody TaskDTO task) {
        try {
            return ResponseEntity.ok(taskService.updateTask(task, id));
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
