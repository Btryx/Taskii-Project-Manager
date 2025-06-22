package com.webfejl.beadando.controller;

import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.request.CollaboratorRequest;
import com.webfejl.beadando.dto.ProjectDTO;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.exception.ProjectNotFoundException;
import com.webfejl.beadando.service.CollaboratorService;
import com.webfejl.beadando.service.ProjectService;
import com.webfejl.beadando.util.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {

    private final ProjectService projectService;
    private final CollaboratorService collaboratorService;

    @Autowired
    public ProjectController(ProjectService projectService, CollaboratorService collaboratorService) {
        this.projectService = projectService;
        this.collaboratorService = collaboratorService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        try {
            return ResponseEntity.ok(projectService.getAllAccessedProjects());
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(projectService.findProject(id));
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/all")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO project){
        try {
            return ResponseEntity.ok(projectService.createProject(project));
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable String id,
            @RequestBody ProjectDTO project) {
        try {
            return ResponseEntity.ok(projectService.updateProject(project, id));
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}/collaborators")
    public ResponseEntity<?> getCollaborators(@PathVariable String id) {
        return ResponseEntity.ok(collaboratorService.getCollaborators(id));
    }

    @PostMapping("/{id}/collaborator")
    public ResponseEntity<?> createCollaborator(@RequestBody CollaboratorRequest collaboratorRequest) {
        return ResponseEntity.ok(collaboratorService.createCollaborator(collaboratorRequest.getProjectId(), collaboratorRequest.getUserId()));
    }
}

