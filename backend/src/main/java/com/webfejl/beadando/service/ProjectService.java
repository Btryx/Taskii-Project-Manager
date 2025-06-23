package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.ProjectDTO;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.Status;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.exception.ProjectNotFoundException;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.StatusRepository;
import com.webfejl.beadando.repository.UserRepository;
import com.webfejl.beadando.util.ProjectAccessUtil;
import com.webfejl.beadando.util.ProjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.webfejl.beadando.util.ProjectAccessUtil.checkIfUserIsLoggedIn;
import static com.webfejl.beadando.util.ProjectAccessUtil.getUsername;

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
        toDoStatus.setStatusName(DONE);
        toDoStatus.setProject(savedProject);
        toDoStatus.setOrderNumber(3);
        statusRepository.save(doneStatus);
    }

    public ProjectDTO updateProject(ProjectDTO projectDTO, String id) throws AuthorizationException, ProjectNotFoundException {
        User user = projectAccessUtil.getAuthenticatedUser();

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + id));

        projectAccessUtil.checkAccess(id, user);

        Project newProject = ProjectMapper.toEntity(projectDTO, project, userRepository);
        return ProjectMapper.toDTO(projectRepository.save(newProject));
    }

    public void deleteProject(String id) throws AuthorizationException, ProjectNotFoundException {
        User user = projectAccessUtil.getAuthenticatedUser();
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException("Project not found with ID: " + id);
        }
        projectAccessUtil.checkAccess(id, user);

        projectRepository.deleteById(id);
    }
}
