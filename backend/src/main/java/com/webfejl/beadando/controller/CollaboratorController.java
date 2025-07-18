package com.webfejl.beadando.controller;

import com.webfejl.beadando.dto.CollaboratorDto;
import com.webfejl.beadando.dto.ProjectDto;
import com.webfejl.beadando.request.CollaboratorRequest;
import com.webfejl.beadando.service.CollaboratorService;
import com.webfejl.beadando.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:4200")
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    @Autowired
    public CollaboratorController(CollaboratorService collaboratorService) {
        this.collaboratorService = collaboratorService;
    }

    @GetMapping("/{projectId}/users")
    public ResponseEntity<?> getCollaboratorsAsUsers(@PathVariable String projectId) {
        return ResponseEntity.ok(collaboratorService.getCollaboratorsAsUser(projectId));
    }

    @GetMapping("/{projectId}/collaborators")
    public ResponseEntity<?> getCollaborators(@PathVariable String projectId) {
        return ResponseEntity.ok(collaboratorService.getCollaborators(projectId));
    }

    @PostMapping("/{projectId}/collaborator")
    public ResponseEntity<?> createCollaborator(@RequestBody CollaboratorDto collaboratorDto) {
        return ResponseEntity.ok(collaboratorService.createCollaborator(collaboratorDto));
    }

    @DeleteMapping("/collaborator/{id}")
    public ResponseEntity<?> deleteCollaborator(@PathVariable String id) {
        return ResponseEntity.ok(collaboratorService.deleteCollaborator(id));
    }
}

