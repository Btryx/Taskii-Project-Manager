package com.webfejl.beadando.controller;


import com.webfejl.beadando.dto.TaskDTO;
import com.webfejl.beadando.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<?> filterTasks(
            @RequestParam String projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer priority
    ) {
        return ResponseEntity.ok(taskService.findAll(projectId, status, priority));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable String id) {
        return ResponseEntity.ok(taskService.findTask(id));
    }

    @PostMapping("/all")
    public ResponseEntity<?> createTask(@RequestBody TaskDTO task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable String id,
            @RequestBody TaskDTO task) {
        return ResponseEntity.ok(taskService.updateTask(task, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
