package com.webfejl.beadando.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class TaskDTO {
    private String taskId;
    private String taskTitle;
    private String taskStatus;
    private int taskPriority;
    private Date taskDate;
    private String taskDesc;
    private int projectId;
}
