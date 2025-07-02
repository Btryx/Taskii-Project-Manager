package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.ProjectDTO;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.Status;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.exception.ProjectNotFoundException;
import com.webfejl.beadando.exception.UserCreationException;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.StatusRepository;
import com.webfejl.beadando.repository.UserRepository;
import com.webfejl.beadando.util.ProjectAccessUtil;
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
    private final ProjectAccessUtil projectAccessUtil;
    private final StatusRepository statusRepository;

    public static final String TO_DO = "To do";
    public static final String IN_PROGRESS = "In progress";
    public static final String DONE = "Done";

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, ProjectAccessUtil projectAccessUtil, StatusRepository statusRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectAccessUtil = projectAccessUtil;
        this.statusRepository = statusRepository;
    }

    public List<ProjectDTO> getAllAccessedProjects() throws AuthorizationException {
        User user = projectAccessUtil.getAuthenticatedUser();

        List<ProjectDTO> ownProjects = projectAccessUtil.getOwnProjects(user);
        List<ProjectDTO> collabProjects = projectAccessUtil.getCollabProjects(user);

        return Stream.concat(ownProjects.stream(), collabProjects.stream()).toList();
    }

    public ProjectDTO findProject(String id) throws ProjectNotFoundException, AuthorizationException {
        User user = projectAccessUtil.getAuthenticatedUser();
        ProjectDTO project = projectRepository.findById(id)
                .map(ProjectMapper::toDTO)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + id));

        projectAccessUtil.checkAccess(id, user);
        return project;
    }

    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) throws AuthorizationException {
        projectAccessUtil.getAuthenticatedUser();
        Project project = ProjectMapper.toEntity(projectDTO, new Project(), userRepository);
        project.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Project savedProject = projectRepository.save(project);

        createDefaultStatuses(savedProject);

        return ProjectMapper.toDTO(savedProject);
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
    public ProjectDTO updateProject(ProjectDTO projectDTO, String id) throws AuthorizationException, ProjectNotFoundException {
        User user = projectAccessUtil.getAuthenticatedUser();

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + id));

        projectAccessUtil.checkAccess(id, user);

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
        User user = projectAccessUtil.getAuthenticatedUser();
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException("Project not found with ID: " + id);
        }
        projectAccessUtil.checkAccess(id, user);

        List<Status> statuses = statusRepository.findByProjectId(id);

        statusRepository.deleteAll(statuses);
        projectRepository.deleteById(id);
    }
}
