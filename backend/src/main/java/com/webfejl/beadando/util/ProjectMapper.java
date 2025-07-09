package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.ProjectDto;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.exception.UserNotFoundException;
import com.webfejl.beadando.repository.UserRepository;

public class ProjectMapper {

    public static ProjectDto toDTO(Project project) {
        return new ProjectDto(
                project.getProjectId(),
                project.getProjectName(),
                project.getProjectDesc(),
                project.getCreatedAt(),
                project.getActive(),
                project.getParentId(),
                project.getUser().getUserId()
        );
    }

    public static Project toEntity(ProjectDto projectDTO, Project project, UserRepository userRepository) {
        project.setProjectId(projectDTO.projectId());
        project.setProjectName(projectDTO.projectName());
        project.setActive(projectDTO.active());
        project.setCreatedAt(projectDTO.createdAt());
        project.setParentId(projectDTO.parentId());
        project.setProjectDesc(projectDTO.projectDesc());

        if (projectDTO.userId() == null) {
            String username = AccessUtil.getUsername();
            project.setUser(userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found")));
            System.out.println("Current user: " + username);
        } else {
            project.setUser(userRepository.findById(projectDTO.userId()).orElseThrow(() -> new UserNotFoundException("User not found")));
        }

        return project;
    }
}
