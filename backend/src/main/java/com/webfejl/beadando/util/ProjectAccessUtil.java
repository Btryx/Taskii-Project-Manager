package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.ProjectDTO;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.repository.CollaboratorRepository;
import com.webfejl.beadando.repository.ProjectRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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

    public static void checkIfUserIsLoggedIn(Optional<User> user) {
        if (user.isEmpty()) {
            throw new AuthorizationException("Please log in to access this content!");
        }
    }


    public static String getUsername() {
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
        return username;
    }
}
