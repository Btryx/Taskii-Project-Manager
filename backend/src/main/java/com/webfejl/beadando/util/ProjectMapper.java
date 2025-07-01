package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.ProjectDTO;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.UserNotFoundException;
import com.webfejl.beadando.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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

        if (projectDTO.userId() == null) {
            String username = ProjectAccessUtil.getUsername();
            project.setUser(userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found")));
            System.out.println("Current user: " + username);
        } else {
            project.setUser(userRepository.findById(projectDTO.userId()).orElseThrow(() -> new UserNotFoundException("User not found")));
        }

        return project;
    }
}
