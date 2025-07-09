package com.webfejl.beadando.dto;

import java.sql.Timestamp;

public record CommentDto(
        String commentId,
        String taskId,
        String comment,
        Timestamp createdAt,
        String userId) {
}