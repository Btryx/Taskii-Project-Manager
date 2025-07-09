package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.TaskDto;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.Task;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.TaskRepository;
import com.webfejl.beadando.util.TaskMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;


import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    private TaskDto taskDTO;
    private Task task;

    @BeforeEach
    void setup() {
        taskDTO = new TaskDto("task1", "Fix bug", "DONE", 1, new Timestamp(System.currentTimeMillis()), "Fix it", null, null, "project1");
        task = new Task();
        task.setTaskId("task1");
        task.setTaskTitle("Fix bug");
        task.setTaskStatus("OPEN");
        task.setTaskPriority(1);
        task.setTaskDate(taskDTO.taskDate());
        task.setTaskDesc("Fix it");
        task.setProject(new Project());
    }

    @Test
    void findAll_shouldReturnFilteredTasks() {
        when(taskRepository.filterTasks("project1", "OPEN", 1)).thenReturn(List.of(task));

        try (MockedStatic<TaskMapper> mocked = mockStatic(TaskMapper.class)) {
            mocked.when(() -> TaskMapper.toDTO(task)).thenReturn(taskDTO);

            List<TaskDto> result = taskService.findAll("project1", "OPEN", 1);

            assertEquals(1, result.size());
            assertEquals("task1", result.get(0).taskId());
        }
    }

    @Test
    void findTask_shouldReturnTaskDTO() {
        when(taskRepository.findById("task1")).thenReturn(Optional.of(task));

        try (MockedStatic<TaskMapper> mocked = mockStatic(TaskMapper.class)) {
            mocked.when(() -> TaskMapper.toDTO(task)).thenReturn(taskDTO);

            TaskDto result = taskService.findTask("task1");

            assertEquals("task1", result.taskId());
        }
    }

    @Test
    void findTask_shouldThrowExceptionWhenNotFound() {
        when(taskRepository.findById("nonexistent")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> taskService.findTask("nonexistent"));
        assertTrue(ex.getMessage().contains("Task not found"));
    }

    @Test
    void createTask_shouldSaveAndReturnTaskDTO() {
        try (MockedStatic<TaskMapper> mocked = mockStatic(TaskMapper.class)) {
            mocked.when(() -> TaskMapper.toEntity(any(TaskDto.class), any(Task.class), any(ProjectRepository.class)))
                    .thenReturn(task);
            mocked.when(() -> TaskMapper.toDTO(task))
                    .thenReturn(taskDTO);

            when(taskRepository.save(task)).thenReturn(task);

            TaskDto result = taskService.createTask(taskDTO);

            assertEquals("task1", result.taskId());
        }
    }

    @Test
    void updateTask_shouldUpdateAndReturnDTO() {
        when(taskRepository.findById("task1")).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        try (MockedStatic<TaskMapper> mocked = mockStatic(TaskMapper.class)) {
            mocked.when(() -> TaskMapper.toEntity(taskDTO, task, projectRepository)).thenReturn(task);
            mocked.when(() -> TaskMapper.toDTO(task)).thenReturn(taskDTO);

            TaskDto result = taskService.updateTask(taskDTO, "task1");

            assertEquals("task1", result.taskId());
        }
    }

    @Test
    void updateTask_shouldThrowExceptionWhenNotFound() {
        when(taskRepository.findById("badid")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> taskService.updateTask(taskDTO, "badid"));
        assertTrue(ex.getMessage().contains("Task not found"));
    }

    @Test
    void deleteTask_shouldDeleteWhenExists() {
        when(taskRepository.existsById("task1")).thenReturn(true);

        taskService.deleteTask("task1");

        verify(taskRepository).deleteById("task1");
    }

    @Test
    void deleteTask_shouldThrowExceptionWhenNotFound() {
        when(taskRepository.existsById("badid")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> taskService.deleteTask("badid"));
        assertTrue(ex.getMessage().contains("Task not found"));
    }
}
