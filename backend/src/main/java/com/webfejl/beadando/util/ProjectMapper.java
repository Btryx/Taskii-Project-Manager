package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.ProjectDTO;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
        }
        project.setUser(userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found")));
        System.out.println("Current user: " + username);
        return project;
    }
}
