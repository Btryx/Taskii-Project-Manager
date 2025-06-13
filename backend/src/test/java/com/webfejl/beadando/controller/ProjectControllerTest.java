package com.webfejl.beadando.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webfejl.beadando.dto.ProjectDTO;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private ProjectRepository projectRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @WithMockUser(username = "testuser", roles = "USER")
    @Test
    void getAllProjectsShouldReturnList() throws Exception {
        ProjectDTO project = new ProjectDTO(
                "1",
                "Test Project",
                Timestamp.from(Instant.now()),
                true,
                null,
                "user123"
        );

        when(projectService.findAll()).thenReturn(List.of(project));

        mockMvc.perform(get("/api/projects/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].projectId").value("1"))
                .andExpect(jsonPath("$[0].projectName").value("Test Project"));
    }

    @WithMockUser(username = "testuser", roles = "USER")
    @Test
    void getProjectByIdShouldReturnProject() throws Exception {
        ProjectDTO project = new ProjectDTO(
                "1",
                "Test Project",
                Timestamp.from(Instant.now()),
                true,
                null,
                "user123"
        );

        when(projectService.findProject("1")).thenReturn(project);

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.projectId").value("1"))
                .andExpect(jsonPath("$.projectName").value("Test Project"));
    }

    @WithMockUser(username = "testuser", roles = "USER")
    @Test
    void createProjectShouldReturnCreatedProject() throws Exception {
        ProjectDTO project = new ProjectDTO(
                null,
                "New Project",
                Timestamp.from(Instant.now()),
                true,
                null,
                "user123"
        );

        ProjectDTO createdProject = new ProjectDTO(
                "123",
                "New Project",
                Timestamp.from(Instant.now()),
                true,
                null,
                "user123"
        );
        when(projectService.createProject(any(ProjectDTO.class))).thenReturn(createdProject);

        mockMvc.perform(post("/api/projects/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value("123"))
                .andExpect(jsonPath("$.projectName").value("New Project"));
    }


    @WithMockUser(username = "testuser", roles = "USER")
    @Test
    void deleteProjectShouldReturnNoContent() throws Exception {
        doNothing().when(projectService).deleteProject("1");

        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isNoContent());
    }

}
