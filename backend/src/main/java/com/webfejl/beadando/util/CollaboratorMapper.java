package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.CollaboratorDto;
import com.webfejl.beadando.entity.Collaborator;
import com.webfejl.beadando.exception.ProjectNotFoundException;
import com.webfejl.beadando.repository.ProjectRepository;

public class CollaboratorMapper {

    public static CollaboratorDto toDTO(Collaborator collaborator) {
        return new CollaboratorDto(
                collaborator.getCollaboratorId(),
                collaborator.getUserId(),
                collaborator.getProject().getProjectId(),
                collaborator.getRole()
        );
    }

    public static Collaborator toEntity(CollaboratorDto collaboratorDto, Collaborator collaborator, ProjectRepository projectRepository) {
        collaborator.setProject(projectRepository.findById(collaboratorDto.projectId()).orElseThrow(() -> new ProjectNotFoundException("Project not found")));
        collaborator.setRole(collaboratorDto.role());
        collaborator.setUserId(collaboratorDto.userId());

        return collaborator;
    }
}
