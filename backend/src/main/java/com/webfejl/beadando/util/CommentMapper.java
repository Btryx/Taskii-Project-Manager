package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.CommentDto;
import com.webfejl.beadando.entity.Comment;
import com.webfejl.beadando.entity.Task;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.TaskNotFoundException;
import com.webfejl.beadando.exception.UserNotFoundException;
import com.webfejl.beadando.repository.TaskRepository;
import com.webfejl.beadando.repository.UserRepository;

public class CommentMapper {

    public static CommentDto toDTO(Comment comment) {
        return new CommentDto(
                comment.getCommentId(),
                comment.getTask() != null ? comment.getTask().getTaskId() : null,
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getUser() != null ? comment.getUser().getUserId() : null
        );
    }

    public static Comment toEntity(CommentDto commentDto, Comment comment,
                                   UserRepository userRepository, TaskRepository taskRepository) {
        comment.setComment(commentDto.comment());
        comment.setCreatedAt(commentDto.createdAt());
        Task task = taskRepository.findById(commentDto.taskId()).orElseThrow(() -> new TaskNotFoundException("Task doesn't exist!"));
        comment.setTask(task);
        User user = userRepository.findById(commentDto.userId()).orElseThrow(() -> new UserNotFoundException("User does not exist!"));
        comment.setUser(user);
        return comment;
    }
}
