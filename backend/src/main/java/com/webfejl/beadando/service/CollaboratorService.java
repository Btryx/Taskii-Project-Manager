package com.webfejl.beadando.service;

import com.webfejl.beadando.entity.Collaborator;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.exception.ProjectNotFoundException;
import com.webfejl.beadando.repository.CollaboratorRepository;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public CollaboratorService(CollaboratorRepository collaboratorRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.collaboratorRepository = collaboratorRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public Collaborator createCollaborator(String projectId, String userId) {
        Collaborator collaborator = new Collaborator();
        collaborator.setUser(userRepository.findById(userId).orElseThrow(() -> new AuthorizationException("User not found!")));
        collaborator.setProject(projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project not found!")));
        //TODO: only create if collaborator isn't already added!
        return collaboratorRepository.save(collaborator);
    }

    public List<User> getCollaborators(String projectId) {
        return collaboratorRepository.findUsersByProjectId(projectId);
    }
}
