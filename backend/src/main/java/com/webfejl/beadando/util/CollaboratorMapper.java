package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.CollaboratorDto;
import com.webfejl.beadando.dto.ProjectDto;
import com.webfejl.beadando.entity.Collaborator;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.exception.ProjectNotFoundException;
import com.webfejl.beadando.exception.UserNotFoundException;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.UserRepository;

public class CollaboratorMapper {

    public static CollaboratorDto toDTO(Collaborator collaborator) {
        return new CollaboratorDto(
                collaborator.getCollaboratorId(),
                collaborator.getUser().getUserId(),
                collaborator.getProject().getProjectId(),
                collaborator.getRole()
        );
    }

    public static Collaborator toEntity(CollaboratorDto collaboratorDto, Collaborator collaborator, UserRepository userRepository, ProjectRepository projectRepository) {
        collaborator.setProject(projectRepository.findById(collaboratorDto.projectId()).orElseThrow(() -> new ProjectNotFoundException("Project not found")));
        collaborator.setRole(collaboratorDto.role());
        collaborator.setUser(userRepository.findById(collaboratorDto.userId()).orElseThrow(() -> new UserNotFoundException("User not found")));

        return collaborator;
    }
}
