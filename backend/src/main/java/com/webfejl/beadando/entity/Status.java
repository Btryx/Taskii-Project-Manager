package com.webfejl.beadando.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Entity
@Table(name = "statuses")
public class Status {

    @Id
    @UuidGenerator
    @Column(name = "status_id", updatable = false, nullable = false)
    private String statusId;

    @Column(name = "status_name", nullable = false)
    private String statusName;

    @Column(name = "order_number")
    private Integer orderNumber;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}