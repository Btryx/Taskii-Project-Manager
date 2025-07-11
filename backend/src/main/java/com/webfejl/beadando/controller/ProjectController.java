package com.webfejl.beadando.controller;

import com.webfejl.beadando.request.CollaboratorRequest;
import com.webfejl.beadando.dto.ProjectDto;
import com.webfejl.beadando.service.CollaboratorService;
import com.webfejl.beadando.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<?> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllAccessedProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable String id) {
        return ResponseEntity.ok(projectService.findProject(id));
    }

    @PostMapping("/all")
    public ResponseEntity<?> createProject(@RequestBody ProjectDto project){
        return ResponseEntity.ok(projectService.createProject(project));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(
            @PathVariable String id,
            @RequestBody ProjectDto project) {
        return ResponseEntity.ok(projectService.updateProject(project, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
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

