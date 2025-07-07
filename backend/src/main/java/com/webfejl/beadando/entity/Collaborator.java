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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "role")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
