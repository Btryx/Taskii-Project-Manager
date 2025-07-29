package com.webfejl.beadando.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Entity
@Table(name = "collaborators")
public class Collaborator {

    @Id
    @UuidGenerator
    @Column(name = "collaborator_id", updatable = false, nullable = false)
    private String collaboratorId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "role")
    private String role;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
