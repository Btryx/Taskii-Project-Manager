package com.webfejl.beadando.service;

import com.webfejl.beadando.entity.Collaborator;
import com.webfejl.beadando.entity.Role;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.exception.ProjectNotFoundException;
import com.webfejl.beadando.exception.UserNotFoundException;
import com.webfejl.beadando.repository.CollaboratorRepository;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.UserRepository;
import com.webfejl.beadando.util.AccessUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final AccessUtil accessUtil;

    public CollaboratorService(CollaboratorRepository collaboratorRepository, UserRepository userRepository, ProjectRepository projectRepository, AccessUtil accessUtil) {
        this.collaboratorRepository = collaboratorRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.accessUtil = accessUtil;
    }

    public Collaborator createCollaborator(String projectId, String userId) throws AuthorizationException {
        User user = accessUtil.getAuthenticatedUser();

        Collaborator collaborator = new Collaborator();
        collaborator.setProject(projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project not found!")));
        accessUtil.checkAccess(projectId, user);

        collaborator.setRole(Role.CONTRIBUTOR);

        collaborator.setUser(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found!")));

        boolean alreadyCollaborator = collaboratorRepository
                .existsByProject_ProjectIdAndUser_UserId(projectId, userId);
        if (alreadyCollaborator) {
            throw new IllegalArgumentException("This user is already a collaborator on the project.");
        }

        return collaboratorRepository.save(collaborator);
    }

    public List<User> getCollaborators(String projectId) throws AuthorizationException {
        User user = accessUtil.getAuthenticatedUser();
        accessUtil.checkAccess(projectId, user);
        return collaboratorRepository.findUsersByProjectId(projectId);
    }
}
