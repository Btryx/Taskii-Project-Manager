package com.webfejl.beadando.dto;

import java.sql.Timestamp;

public record StatusDto(
        String statusId,
        String statusName,
        Integer orderNumber,
        String projectId) {
}