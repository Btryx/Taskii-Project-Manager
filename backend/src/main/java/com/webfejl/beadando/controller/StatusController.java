package com.webfejl.beadando.controller;


import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statuses")
@CrossOrigin(origins = "http://localhost:4200")
public class StatusController {

    private final StatusService service;

    @Autowired
    public StatusController(StatusService service) {
        this.service = service;
    }

    @GetMapping("{projectId}/all")
    public ResponseEntity<?> getAllProjectStatuses(@PathVariable String projectId) {
        try {
            return ResponseEntity.ok(service.getStatusesForProject(projectId));
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    //TODO: create, update and delete statuses in project
}
