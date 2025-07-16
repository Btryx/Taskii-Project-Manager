package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.ProjectDto;
import com.webfejl.beadando.entity.Role;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.exception.ProjectAccessDenied;
import com.webfejl.beadando.exception.UserNotFoundException;
import com.webfejl.beadando.repository.CollaboratorRepository;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AccessUtil {
    private final ProjectRepository projectRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final UserRepository userRepository;

    public AccessUtil(ProjectRepository projectRepository, CollaboratorRepository collaboratorRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.userRepository = userRepository;
    }

    private boolean isProjectAccessGranted(String projectId, User user) {
        boolean isOwner = projectRepository.existsByProjectIdAndUser_UserId(projectId, user.getUserId());
        boolean isCollaborator = collaboratorRepository.existsByProject_ProjectIdAndUser_UserId(projectId, user.getUserId());
        return isOwner || isCollaborator;
    }

    public boolean isAdmin(String projectId, String userId, Role role) {
        return collaboratorRepository.isUserAdminOnProject(userId, projectId, role.name());
    }

    public void checkAccess(String projectId, User user) throws ProjectAccessDenied {
        if (!isProjectAccessGranted(projectId, user)) {
            throw new ProjectAccessDenied("You don't have access to this project!");
        }
    }

    public List<ProjectDto> getOwnProjects(User user) {
        return projectRepository.findByUserId(user.getUserId())
                .stream()
                .map(ProjectMapper::toDTO)
                .toList();
    }

    public List<ProjectDto> getCollabProjects(User user) {
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

    public User getAuthenticatedUser() {
        String username = getUsername();
        if (username == null) {
            throw new AuthorizationException("Please log in to access this content!");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
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
