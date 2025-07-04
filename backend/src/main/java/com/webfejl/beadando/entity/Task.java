package com.webfejl.beadando.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @UuidGenerator
    @Column(name = "task_id", updatable = false, nullable = false)
    private String taskId;

    @Column(name = "task_title", nullable = false)
    private String taskTitle;

    //TODO: this should be a foreign key to Status entity, change after fully migrating to dynamic statuses on frontend
    @Column(name = "task_status", nullable = false)
    private String taskStatus;

    @Column(name = "task_priority", nullable = false)
    private Integer taskPriority;

    @Column(name = "task_date", nullable = true)
    private Timestamp taskDate;

    @Column(name = "task_desc")
    private String taskDesc;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Task task = (Task) o;
        return taskId != null && Objects.equals(taskId, task.taskId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
