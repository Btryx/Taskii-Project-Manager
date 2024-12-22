package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.TaskDTO;
import com.webfejl.beadando.entity.Task;

public class TaskMapper {

    public static TaskDTO toDTO(Task task) {
        return new TaskDTO(
                task.getTaskId(),
                task.getTaskTitle(),
                task.getTaskStatus(),
                task.getTaskPriority(),
                task.getTaskDate(),
                task.getTaskDesc()
        );
    }

    public static Task toEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTaskTitle(taskDTO.getTaskTitle());
        task.setTaskStatus(taskDTO.getTaskStatus());
        task.setTaskPriority(taskDTO.getTaskPriority());
        task.setTaskDate(taskDTO.getTaskDate());
        task.setTaskDesc(taskDTO.getTaskDesc());
        return task;
    }
}
