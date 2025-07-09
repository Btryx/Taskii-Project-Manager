package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.TaskDto;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.Task;
import com.webfejl.beadando.exception.ProjectNotFoundException;
import com.webfejl.beadando.repository.ProjectRepository;

public class TaskMapper {

    public static TaskDto toDTO(Task task) {
        return new TaskDto(
                task.getTaskId(),
                task.getTaskTitle(),
                task.getTaskStatus(),
                task.getTaskPriority(),
                task.getTaskDate(),
                task.getTaskDesc(),
                task.getAssignee(),
                task.getOrderNumber(),
                task.getProject() != null ? task.getProject().getProjectId() : null
        );
    }

    public static Task toEntity(TaskDto taskDTO, Task task, ProjectRepository projectRepository) {
        task.setTaskId(taskDTO.taskId());
        task.setTaskTitle(taskDTO.taskTitle());
        task.setTaskStatus(taskDTO.taskStatus());
        task.setTaskPriority(taskDTO.taskPriority());
        task.setTaskDate(taskDTO.taskDate());
        task.setTaskDesc(taskDTO.taskDesc());
        task.setOrderNumber(taskDTO.orderNumber());
        task.setAssignee(taskDTO.assignee());
        Project project = projectRepository.findById(taskDTO.projectId()).orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        task.setProject(project);
        return task;
    }
}
