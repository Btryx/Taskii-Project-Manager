package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.ProjectDto;
import com.webfejl.beadando.entity.*;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.exception.ProjectNotFoundException;
import com.webfejl.beadando.exception.UserCreationException;
import com.webfejl.beadando.repository.*;
import com.webfejl.beadando.util.AccessUtil;
import com.webfejl.beadando.util.ProjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Stream;


@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final AccessUtil accessUtil;
    private final StatusRepository statusRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final TaskRepository taskRepository;

    public static final String TO_DO = "To do";
    public static final String IN_PROGRESS = "In progress";
    public static final String DONE = "Done";

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, AccessUtil accessUtil,
                          StatusRepository statusRepository, CollaboratorRepository collaboratorRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.accessUtil = accessUtil;
        this.statusRepository = statusRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.taskRepository = taskRepository;
    }

    public List<ProjectDto> getAllAccessedProjects() throws AuthorizationException {
        User user = accessUtil.getAuthenticatedUser();

        return accessUtil.getCollabProjects(user);
    }

    public ProjectDto findProject(String id) throws ProjectNotFoundException, AuthorizationException {
        User user = accessUtil.getAuthenticatedUser();
        ProjectDto project = projectRepository.findById(id)
                .map(ProjectMapper::toDTO)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + id));

        accessUtil.checkAccess(id, user);
        return project;
    }

    @Transactional
    public ProjectDto createProject(ProjectDto projectDTO) throws AuthorizationException {
        accessUtil.getAuthenticatedUser();
        Project project = ProjectMapper.toEntity(projectDTO, new Project(), userRepository);
        project.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Project savedProject = projectRepository.save(project);

        createDefaultStatuses(savedProject);
        addOwnerAsCollaborator(project);

        return ProjectMapper.toDTO(savedProject);
    }

    public Boolean isAdmin(String projectId) throws AuthorizationException {
        return accessUtil.isAdmin(projectId, accessUtil.getAuthenticatedUser().getUserId(), Role.ADMIN);
    }

    private void addOwnerAsCollaborator(Project project) {
        Collaborator collaborator = new Collaborator();
        collaborator.setProject(project);
        collaborator.setUser(project.getUser());
        collaborator.setRole(Role.ADMIN.name());
        collaboratorRepository.save(collaborator);
    }

    private void createDefaultStatuses(Project savedProject) {
        Status toDoStatus = new Status();
        toDoStatus.setStatusName(TO_DO);
        toDoStatus.setProject(savedProject);
        toDoStatus.setOrderNumber(1);
        statusRepository.save(toDoStatus);

        Status inProgressStatus = new Status();
        inProgressStatus.setStatusName(IN_PROGRESS);
        inProgressStatus.setProject(savedProject);
        inProgressStatus.setOrderNumber(2);
        statusRepository.save(inProgressStatus);

        Status doneStatus = new Status();
        doneStatus.setStatusName(DONE);
        doneStatus.setProject(savedProject);
        doneStatus.setOrderNumber(3);
        statusRepository.save(doneStatus);
    }

    @Transactional
    public ProjectDto updateProject(ProjectDto projectDTO, String id) throws AuthorizationException, ProjectNotFoundException {
        User user = accessUtil.getAuthenticatedUser();

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + id));

        accessUtil.checkAccess(id, user);

        try{
            Project newProject = ProjectMapper.toEntity(projectDTO, project, userRepository);
            Project saved = projectRepository.save(newProject);
            projectRepository.flush();

            return ProjectMapper.toDTO(saved);
        } catch (DataIntegrityViolationException e) {
            throw new UserCreationException("You already have a project named: " + projectDTO.projectName());
        }


    }

    @Transactional
    public void deleteProject(String id) throws AuthorizationException, ProjectNotFoundException {
        User user = accessUtil.getAuthenticatedUser();
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException("Project not found with ID: " + id);
        }
        accessUtil.checkAccess(id, user);

        List<Status> statuses = statusRepository.findByProjectId(id);

        statusRepository.deleteAll(statuses);
        collaboratorRepository.deleteAllByProject_ProjectId(id);
        taskRepository.deleteAllByProject_ProjectId(id);

        projectRepository.deleteById(id);
    }
}
