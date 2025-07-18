package com.webfejl.beadando.dto;

import java.sql.Timestamp;

public record CollaboratorDto(
        String collaboratorId,
        String userId,
        String projectId,
        String role) {
}