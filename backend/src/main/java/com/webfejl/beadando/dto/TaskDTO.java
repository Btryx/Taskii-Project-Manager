package com.webfejl.beadando.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class TaskDTO {

    private String taskId;
    private String taskTitle;
    private String taskStatus;
    private int taskPriority;
    private Date taskDate;
    private String taskDesc;

    public TaskDTO(String taskId, String taskTitle, String taskStatus, int taskPriority, Date taskDate, String taskDesc) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskStatus = taskStatus;
        this.taskPriority = taskPriority;
        this.taskDate = taskDate;
        this.taskDesc = taskDesc;
    }
}
