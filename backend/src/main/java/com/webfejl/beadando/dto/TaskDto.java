package com.webfejl.beadando.dto;

import java.sql.Timestamp;


public record TaskDto(
        String taskId,
        String taskTitle,
        String taskStatus,
        int taskPriority,
        Timestamp taskDate,
        String taskDesc,
        String assignee,
        Integer orderNumber,
        String projectId) {
}
