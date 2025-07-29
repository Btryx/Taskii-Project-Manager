package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.CommentDto;
import com.webfejl.beadando.entity.Comment;
import com.webfejl.beadando.entity.Task;
import com.webfejl.beadando.exception.TaskNotFoundException;
import com.webfejl.beadando.repository.TaskRepository;

public class CommentMapper {

    public static CommentDto toDTO(Comment comment) {
        return new CommentDto(
                comment.getCommentId(),
                comment.getTask() != null ? comment.getTask().getTaskId() : null,
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getUserId()
        );
    }

    public static Comment toEntity(CommentDto commentDto, Comment comment, TaskRepository taskRepository) {
        comment.setComment(commentDto.comment());
        comment.setCreatedAt(commentDto.createdAt());
        Task task = taskRepository.findById(commentDto.taskId()).orElseThrow(() -> new TaskNotFoundException("Task doesn't exist!"));
        comment.setTask(task);
        comment.setUserId(commentDto.userId());
        return comment;
    }
}
