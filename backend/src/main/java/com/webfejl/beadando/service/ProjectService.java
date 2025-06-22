package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.ProjectDTO;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.exception.ProjectNotFoundException;
import com.webfejl.beadando.repository.ProjectRepository;
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

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectAccessUtil projectAccessUtil;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, ProjectAccessUtil projectAccessUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectAccessUtil = projectAccessUtil;
    }

    public List<ProjectDTO> getAllAccessedProjects() {

        String username = getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<ProjectDTO> ownProjects = projectAccessUtil.getOwnProjects(user);

        List<ProjectDTO> collabProjects = projectAccessUtil.getCollabProjects(user);

        return Stream.concat(ownProjects.stream(), collabProjects.stream()).toList();
    }

    private static String getUsername() {
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

    public ProjectDTO findProject(String id) throws ProjectNotFoundException, AuthorizationException {
        ProjectDTO project = projectRepository.findById(id)
                .map(ProjectMapper::toDTO)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + id));

        Optional<User> user = userRepository.findByUsername(getUsername());
        if (user.isEmpty()) {
            throw new AuthorizationException("Please log in to see this project!");
        }
        if (!projectAccessUtil.isProjectAccessGranted(project, user.get())) {
            throw new AuthorizationException("You don't have access to this project!");
        }
        return project;
    }

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = ProjectMapper.toEntity(projectDTO, new Project(), userRepository);
        project.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Project savedProject = projectRepository.save(project);
        return ProjectMapper.toDTO(savedProject);
    }

    public ProjectDTO updateProject(ProjectDTO projectDTO, String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));

        Project newProject = ProjectMapper.toEntity(projectDTO, project, userRepository);

        return ProjectMapper.toDTO(projectRepository.save(newProject));
    }

    public void deleteProject(String id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found with ID: " + id);
        }
        projectRepository.deleteById(id);
    }
}
