package com.webfejl.beadando.controller;


import com.webfejl.beadando.dto.TaskDTO;
import com.webfejl.beadando.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok(taskService.findAll(projectId, status, priority));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable String id) {
        return ResponseEntity.ok(taskService.findTask(id));
    }

    @GetMapping("/sort/title")
    public ResponseEntity<List<TaskDTO>> getTasksSortedByTitle() {
        return ResponseEntity.ok(taskService.sortTasksByTitle());
    }

    @GetMapping("/sort/date")
    public ResponseEntity<List<TaskDTO>> getTasksSortedByDate() {
        return ResponseEntity.ok(taskService.sortTasksByDate());
    }

    @PostMapping("/all")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO task){
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable String id,
            @RequestBody TaskDTO task) {
        return ResponseEntity.ok(taskService.updateTask(task, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
