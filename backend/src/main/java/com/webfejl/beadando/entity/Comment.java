package com.webfejl.beadando.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @UuidGenerator
    @Column(name = "comment_id", updatable = false, nullable = false)
    private String commentId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "user_id")
    private String userId;
}
