package com.webfejl.beadando.dto;

import java.sql.Timestamp;

public record ProjectDTO(
        String projectId,
        String projectName,
        Timestamp createdAt,
        Boolean active,
        String parentId,
        String userId) {
}
