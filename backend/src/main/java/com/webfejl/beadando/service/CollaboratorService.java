package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.CollaboratorDto;
import com.webfejl.beadando.entity.Collaborator;
import com.webfejl.beadando.entity.Role;
import com.webfejl.beadando.entity.Task;
import com.webfejl.beadando.exception.*;
import com.webfejl.beadando.repository.CollaboratorRepository;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.TaskRepository;
import com.webfejl.beadando.util.AccessUtil;
import com.webfejl.beadando.util.CollaboratorMapper;
import jakarta.transaction.Transactional;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final AccessUtil accessUtil;
    private final KeycloakService keycloakService;

    public CollaboratorService(CollaboratorRepository collaboratorRepository, ProjectRepository projectRepository,
                               TaskRepository taskRepository, AccessUtil accessUtil, KeycloakService keycloakService) {
        this.collaboratorRepository = collaboratorRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.accessUtil = accessUtil;
        this.keycloakService = keycloakService;
    }


    public List<UserRepresentation> getCollaboratorsAsUser(String projectId) throws AuthorizationException {
        String userId = accessUtil.getAuthenticatedUser();
        accessUtil.checkAccess(projectId, userId);
        List<String> userIds = collaboratorRepository.findUsersByProjectId(projectId);
        List<UserRepresentation> users = new ArrayList<>();
        for (String id : userIds) {
            UserRepresentation user = keycloakService.getUserById(id);
            if (user.isEnabled()) {
                users.add(user);
            }
        }
        return users;
    }

    public List<CollaboratorDto> getCollaborators(String projectId) throws AuthorizationException {
        String user = accessUtil.getAuthenticatedUser();
        accessUtil.checkAccess(projectId, user);
        return collaboratorRepository.findAllByProjectId(projectId).stream().map(CollaboratorMapper::toDTO).toList();
    }

    @Transactional
    public Collaborator createCollaborator(CollaboratorDto collaboratorDto) throws AuthorizationException {
        String user = accessUtil.getAuthenticatedUser();
        accessUtil.checkAccess(collaboratorDto.projectId(), user);

        if(!accessUtil.isAdmin(collaboratorDto.projectId(), user, Role.ADMIN)) {
            throw new PermissionDeniedException("You don't have permission to add members!");
        }

        Collaborator collaborator = CollaboratorMapper.toEntity(collaboratorDto, new Collaborator(), projectRepository);

        boolean alreadyCollaborator = collaboratorRepository
                .existsByProject_ProjectIdAndUserId(collaboratorDto.projectId(), collaboratorDto.userId());
        if (alreadyCollaborator) {
            throw new IllegalArgumentException("This user is already a collaborator on the project.");
        }

        return collaboratorRepository.save(collaborator);
    }

    @Transactional
    public Collaborator deleteCollaborator(String collaboratorId) throws AuthorizationException {
        String user = accessUtil.getAuthenticatedUser();

        Collaborator collaborator = collaboratorRepository.findById(collaboratorId).orElseThrow(() -> new UserNotFoundException("Member not found!"));

        if(!accessUtil.isAdmin(collaborator.getProject().getProjectId(), user, Role.ADMIN)) {
            throw new PermissionDeniedException("You don't have permission to remove members!");
        }

        if(Objects.equals(user, collaborator.getUserId())) {
            throw new PermissionDeniedException("You can't delete your own account from members!");
        }

        if(Objects.equals(collaborator.getProject().getUserId(), collaborator.getUserId())) {
            throw new PermissionDeniedException("You can't delete project owner from members!");
        }

        if(!accessUtil.isAdmin(collaborator.getProject().getProjectId(), user, Role.ADMIN)) {
            throw new PermissionDeniedException("You don't have permission to add members!");
        }

        List<Task> tasks = taskRepository.findAllByAssignee(collaborator.getUserId());

        for (Task task : tasks) {
            task.setAssignee("");
            taskRepository.save(task);
        }

        collaboratorRepository.delete(collaborator);
        return collaborator;
    }

}
