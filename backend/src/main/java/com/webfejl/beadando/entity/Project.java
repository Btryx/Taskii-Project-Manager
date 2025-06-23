package com.webfejl.beadando.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @UuidGenerator
    @Column(name = "project_id", updatable = false, nullable = false)
    private String projectId = UUID.randomUUID().toString();

    @Column(name = "project_name", nullable = false, unique = true)
    private String projectName;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "parent_id")
    private String parentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Project project = (Project) o;
        return projectId != null && Objects.equals(projectId, project.projectId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

