package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.ProjectDTO;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.repository.CollaboratorRepository;
import com.webfejl.beadando.repository.ProjectRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectAccessUtil {
    private final ProjectRepository projectRepository;
    private final CollaboratorRepository collaboratorRepository;

    public ProjectAccessUtil(ProjectRepository projectRepository, CollaboratorRepository collaboratorRepository) {
        this.projectRepository = projectRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    public boolean isProjectAccessGranted(ProjectDTO projectDTO, User user) {
        if (getOwnProjects(user).contains(projectDTO)) {
            return true;
        }
        return getCollabProjects(user).contains(projectDTO);
    }

    public List<ProjectDTO> getOwnProjects(User user) {
        return projectRepository.findByUserId(user.getUserId())
                .stream()
                .map(ProjectMapper::toDTO)
                .toList();
    }

    public List<ProjectDTO> getCollabProjects(User user) {
        return collaboratorRepository.findProjectsByUserId(user.getUserId())
                .stream()
                .map(ProjectMapper::toDTO)
                .toList();
    }
}
