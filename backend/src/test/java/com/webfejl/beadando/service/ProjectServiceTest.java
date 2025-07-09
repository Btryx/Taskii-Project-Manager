package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.ProjectDto;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.UserRepository;
import com.webfejl.beadando.util.ProjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    private static final String USER_ID = "user123";
    private static final String PROJECT_ID = "proj1";

    private final User mockUser = new User() {{
        setUserId(USER_ID);
        setUsername("testuser");
    }};

    private final Project mockProject = new Project() {{
        setProjectId(PROJECT_ID);
        setProjectName("Project Test");
        setUser(mockUser);
        setCreatedAt(new Timestamp(System.currentTimeMillis()));
        setActive(true);
    }};

    private final ProjectDto mockDTO = new ProjectDto(
            PROJECT_ID, "Project Test", "Project Test Desc",
            new Timestamp(System.currentTimeMillis()), true,
            null, USER_ID
    );

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void findAll_ShouldReturnListOfProjectDTOs() {

        var auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("testuser");

        var securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(projectRepository.findByUserId(USER_ID)).thenReturn(List.of(mockProject));

        try (MockedStatic<ProjectMapper> mockedMapper = Mockito.mockStatic(ProjectMapper.class)) {
            mockedMapper.when(() -> ProjectMapper.toDTO(mockProject)).thenReturn(mockDTO);

            List<ProjectDto> result = projectService.getAllAccessedProjects();

            assertEquals(1, result.size());
            assertEquals(PROJECT_ID, result.get(0).projectId());
        }
    }

    @Test
    void findProject_ShouldReturnDTO_WhenFound() {
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(mockProject));

        try (MockedStatic<ProjectMapper> mockedMapper = Mockito.mockStatic(ProjectMapper.class)) {
            mockedMapper.when(() -> ProjectMapper.toDTO(mockProject)).thenReturn(mockDTO);

            ProjectDto result = projectService.findProject(PROJECT_ID);
            assertEquals(PROJECT_ID, result.projectId());
        }
    }

    @Test
    void findProject_ShouldThrow_WhenNotFound() {
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> projectService.findProject(PROJECT_ID));
        assertTrue(ex.getMessage().contains("Project not found"));
    }

    @Test
    void createProject_ShouldSaveAndReturnDTO() {
        Project newProject = new Project();

        try (MockedStatic<ProjectMapper> mockedMapper = Mockito.mockStatic(ProjectMapper.class)) {
            mockedMapper.when(() -> ProjectMapper.toEntity(eq(mockDTO), any(Project.class), eq(userRepository)))
                    .thenReturn(newProject);
            mockedMapper.when(() -> ProjectMapper.toDTO(any(Project.class))).thenReturn(mockDTO);

            when(projectRepository.save(newProject)).thenReturn(mockProject);

            ProjectDto result = projectService.createProject(mockDTO);

            assertEquals(PROJECT_ID, result.projectId());
            verify(projectRepository).save(newProject);
        }
    }

    @Test
    void updateProject_ShouldUpdateAndReturnDTO() {
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(mockProject));

        try (MockedStatic<ProjectMapper> mockedMapper = Mockito.mockStatic(ProjectMapper.class)) {
            mockedMapper.when(() -> ProjectMapper.toEntity(mockDTO, mockProject, userRepository))
                    .thenReturn(mockProject);
            mockedMapper.when(() -> ProjectMapper.toDTO(mockProject)).thenReturn(mockDTO);

            when(projectRepository.save(mockProject)).thenReturn(mockProject);

            ProjectDto result = projectService.updateProject(mockDTO, PROJECT_ID);

            assertEquals(PROJECT_ID, result.projectId());
        }
    }

    @Test
    void updateProject_ShouldThrow_WhenNotFound() {
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> projectService.updateProject(mockDTO, PROJECT_ID));

        assertTrue(ex.getMessage().contains("Project not found"));
    }

    @Test
    void deleteProject_ShouldDelete_WhenExists() {
        when(projectRepository.existsById(PROJECT_ID)).thenReturn(true);

        projectService.deleteProject(PROJECT_ID);

        verify(projectRepository).deleteById(PROJECT_ID);
    }

    @Test
    void deleteProject_ShouldThrow_WhenNotExists() {
        when(projectRepository.existsById(PROJECT_ID)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> projectService.deleteProject(PROJECT_ID));

        assertTrue(ex.getMessage().contains("Project not found"));
    }
}

