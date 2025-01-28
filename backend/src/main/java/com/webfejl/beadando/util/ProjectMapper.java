package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.ProjectDTO;
import com.webfejl.beadando.dto.TaskDTO;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.Task;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.UserRepository;

public class ProjectMapper {

    public static ProjectDTO toDTO(Project project) {
        return new ProjectDTO(
                project.getProjectId(),
                project.getProjectName(),
                project.getCreatedAt(),
                project.getActive(),
                project.getParentId(),
                project.getUser().getUserId()
        );
    }

    public static Project toEntity(ProjectDTO projectDTO, Project project, UserRepository userRepository) {
        project.setProjectId(projectDTO.projectId());
        project.setProjectName(projectDTO.projectName());
        project.setActive(projectDTO.active());
        project.setCreatedAt(projectDTO.createdAt());
        project.setParentId(projectDTO.parentId());
        //todo: real user
        project.setUser(userRepository.findById("1").orElseThrow(() -> new RuntimeException("User not found")));
        return project;
    }
}
