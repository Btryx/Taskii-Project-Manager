package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.CollaboratorDto;
import com.webfejl.beadando.entity.Collaborator;
import com.webfejl.beadando.entity.Role;
import com.webfejl.beadando.entity.Task;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.*;
import com.webfejl.beadando.repository.CollaboratorRepository;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.TaskRepository;
import com.webfejl.beadando.repository.UserRepository;
import com.webfejl.beadando.util.AccessUtil;
import com.webfejl.beadando.util.CollaboratorMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final AccessUtil accessUtil;

    public CollaboratorService(CollaboratorRepository collaboratorRepository, UserRepository userRepository,
                               ProjectRepository projectRepository, TaskRepository taskRepository, AccessUtil accessUtil) {
        this.collaboratorRepository = collaboratorRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.accessUtil = accessUtil;
    }


    public List<User> getCollaboratorsAsUser(String projectId) throws AuthorizationException {
        User user = accessUtil.getAuthenticatedUser();
        accessUtil.checkAccess(projectId, user);
        return collaboratorRepository.findUsersByProjectId(projectId);
    }

    public List<CollaboratorDto> getCollaborators(String projectId) throws AuthorizationException {
        User user = accessUtil.getAuthenticatedUser();
        accessUtil.checkAccess(projectId, user);
        return collaboratorRepository.findAllByProjectId(projectId).stream().map(CollaboratorMapper::toDTO).toList();
    }

    @Transactional
    public Collaborator createCollaborator(CollaboratorDto collaboratorDto) throws AuthorizationException {
        User user = accessUtil.getAuthenticatedUser();
        accessUtil.checkAccess(collaboratorDto.projectId(), user);

        if(!accessUtil.isAdmin(collaboratorDto.projectId(), user.getUserId(), Role.ADMIN)) {
            throw new PermissionDeniedException("You don't have permission to add members!");
        }

        Collaborator collaborator = CollaboratorMapper.toEntity(collaboratorDto, new Collaborator(), userRepository, projectRepository);

        boolean alreadyCollaborator = collaboratorRepository
                .existsByProject_ProjectIdAndUser_UserId(collaboratorDto.projectId(), collaboratorDto.userId());
        if (alreadyCollaborator) {
            throw new IllegalArgumentException("This user is already a collaborator on the project.");
        }

        return collaboratorRepository.save(collaborator);
    }

    @Transactional
    public Collaborator deleteCollaborator(String collaboratorId) throws AuthorizationException {
        User user = accessUtil.getAuthenticatedUser();

        Collaborator collaborator = collaboratorRepository.findById(collaboratorId).orElseThrow(() -> new UserNotFoundException("Member not found!"));

        if(!accessUtil.isAdmin(collaborator.getProject().getProjectId(), user.getUserId(), Role.ADMIN)) {
            throw new PermissionDeniedException("You don't have permission to remove members!");
        }

        if(Objects.equals(user.getUserId(), collaborator.getUser().getUserId())) {
            throw new PermissionDeniedException("You can't delete your own account from members!");
        }

        if(Objects.equals(collaborator.getProject().getUser().getUserId(), collaborator.getUser().getUserId())) {
            throw new PermissionDeniedException("You can't delete project owner from members!");
        }

        if(!accessUtil.isAdmin(collaborator.getProject().getProjectId(), user.getUserId(), Role.ADMIN)) {
            throw new PermissionDeniedException("You don't have permission to add members!");
        }

        List<Task> tasks = taskRepository.findAllByAssignee(collaborator.getUser().getUserId());

        for (Task task : tasks) {
            task.setAssignee("");
            taskRepository.save(task);
        }

        collaboratorRepository.delete(collaborator);
        return collaborator;
    }

}
