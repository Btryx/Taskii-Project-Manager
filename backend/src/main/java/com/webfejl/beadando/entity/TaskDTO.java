package com.webfejl.beadando.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@Table("tasks")
public class TaskDTO {

    @Id
    private @Column("task_id") String taskId= UUID.randomUUID().toString();
    private @Column("task_title") String taskTitle;
    private @Column("task_status") String taskStatus;
    private @Column("task_priority") int taskPriority;
    private @Column("task_date") Date taskDate;
    private @Column("task_desc") String taskDesc;

    public TaskDTO(String taskTitle, String taskStatus, int taskPriority, Date taskDate, String taskDesc) {
        this.taskTitle = taskTitle;
        this.taskStatus = taskStatus;
        this.taskPriority = taskPriority;
        this.taskDate = taskDate;
        this.taskDesc = taskDesc;
    }

}
