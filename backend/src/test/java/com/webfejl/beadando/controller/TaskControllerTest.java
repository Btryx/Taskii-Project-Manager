package com.webfejl.beadando.controller;

import com.webfejl.beadando.dto.TaskDTO;
import com.webfejl.beadando.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    private final TaskDTO sampleTask = new TaskDTO(
            "task123",
            "Sample Task",
            "IN_PROGRESS",
            2,
            Timestamp.valueOf("2024-01-01 10:00:00"),
            "Sample description",
            "project456"
    );

    @WithMockUser
    @Test
    void filterTasksShouldReturnFilteredTasks() throws Exception {
        when(taskService.findAll("project456", "IN_PROGRESS", 2))
                .thenReturn(List.of(sampleTask));

        mockMvc.perform(get("/api/tasks/filter")
                        .param("projectId", "project456")
                        .param("status", "IN_PROGRESS")
                        .param("priority", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskId").value("task123"));
    }

    @WithMockUser
    @Test
    void getTaskByIdShouldReturnTask() throws Exception {
        when(taskService.findTask("task123")).thenReturn(sampleTask);

        mockMvc.perform(get("/api/tasks/task123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskTitle").value("Sample Task"));
    }

    @WithMockUser
    @Test
    void getTasksSortedByTitleShouldReturnTasks() throws Exception {
        when(taskService.sortTasksByTitle()).thenReturn(List.of(sampleTask));

        mockMvc.perform(get("/api/tasks/sort/title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskTitle").value("Sample Task"));
    }

    @WithMockUser
    @Test
    void getTasksSortedByDateShouldReturnTasks() throws Exception {
        when(taskService.sortTasksByDate()).thenReturn(List.of(sampleTask));

        mockMvc.perform(get("/api/tasks/sort/date"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskId").value("task123"));
    }

    @WithMockUser
    @Test
    void createTaskShouldReturnCreatedTask() throws Exception {
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(sampleTask);

        String json = """
                {
                  "taskId": "task123",
                  "taskTitle": "Sample Task",
                  "taskStatus": "IN_PROGRESS",
                  "taskPriority": 2,
                  "taskDate": "2024-01-01T10:00:00.000+00:00",
                  "taskDesc": "Sample description",
                  "projectId": "project456"
                }
                """;

        mockMvc.perform(post("/api/tasks/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value("task123"));
    }

    @WithMockUser
    @Test
    void updateTaskShouldReturnUpdatedTask() throws Exception {
        when(taskService.updateTask(any(TaskDTO.class), eq("task123")))
                .thenReturn(sampleTask);

        String json = """
                {
                  "taskId": "task123",
                  "taskTitle": "Sample Task",
                  "taskStatus": "IN_PROGRESS",
                  "taskPriority": 2,
                  "taskDate": "2024-01-01T10:00:00.000+00:00",
                  "taskDesc": "Sample description",
                  "projectId": "project456"
                }
                """;

        mockMvc.perform(put("/api/tasks/task123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskStatus").value("IN_PROGRESS"));
    }

    @WithMockUser
    @Test
    void deleteTaskShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/tasks/task123"))
                .andExpect(status().isNoContent());
    }
}

