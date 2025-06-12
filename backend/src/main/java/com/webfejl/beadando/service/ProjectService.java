package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.ProjectDTO;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.UserRepository;
import com.webfejl.beadando.util.ProjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<ProjectDTO> findAll() {

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

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return projectRepository.findByUserId(user.getUserId())
                .stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProjectDTO findProject(String id) {
        return projectRepository.findById(id)
                .map(ProjectMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
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
