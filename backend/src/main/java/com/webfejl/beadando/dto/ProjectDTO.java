package com.webfejl.beadando.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ProjectDTO {
    private int projectId;
    private String projectName;
    private Date createdAt;
    private boolean active;
    private int parentId;
}
