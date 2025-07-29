package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.ProjectDto;
import com.webfejl.beadando.entity.Role;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.exception.ProjectAccessDenied;
import com.webfejl.beadando.repository.CollaboratorRepository;
import com.webfejl.beadando.repository.ProjectRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccessUtil {
    private final ProjectRepository projectRepository;
    private final CollaboratorRepository collaboratorRepository;

    public AccessUtil(ProjectRepository projectRepository, CollaboratorRepository collaboratorRepository) {
        this.projectRepository = projectRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    private boolean isProjectAccessGranted(String projectId, String user) {
        boolean isOwner = projectRepository.existsByProjectIdAndUserId(projectId, user);
        boolean isCollaborator = collaboratorRepository.existsByProject_ProjectIdAndUserId(projectId, user);
        return isOwner || isCollaborator;
    }

    public boolean isAdmin(String projectId, String userId, Role role) {
        return collaboratorRepository.isUserAdminOnProject(userId, projectId, role.name());
    }

    public void checkAccess(String projectId, String user) throws ProjectAccessDenied {
        if (!isProjectAccessGranted(projectId, user)) {
            throw new ProjectAccessDenied("You don't have access to this project!");
        }
    }

    public List<ProjectDto> getOwnProjects(String user) {
        return projectRepository.findByUserId(user)
                .stream()
                .map(ProjectMapper::toDTO)
                .toList();
    }

    public List<ProjectDto> getCollabProjects(String user) {
        return collaboratorRepository.findProjectsByUserId(user)
                .stream()
                .map(ProjectMapper::toDTO)
                .toList();
    }

    public static void checkIfUserIsLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthorizationException("Please log in to access this content!");
        }
    }

    public String  getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
                return jwt.getSubject();
            } else {
                return principal.toString();
            }
        }
        return null;
    }

    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
                return jwt.getClaimAsString("preferred_username");
            } else {
                return principal.toString();
            }
        }
        return null;
    }
}
