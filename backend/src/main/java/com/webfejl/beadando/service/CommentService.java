package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.CommentDto;
import com.webfejl.beadando.entity.Comment;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.repository.CommentRepository;
import com.webfejl.beadando.repository.TaskRepository;
import com.webfejl.beadando.util.AccessUtil;
import com.webfejl.beadando.util.CommentMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final AccessUtil accessUtil;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    public CommentService(AccessUtil accessUtil, TaskRepository taskRepository, CommentRepository commentRepository) {
        this.accessUtil = accessUtil;
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
    }


    @Transactional
    public CommentDto createComment(CommentDto commentDto) throws AuthorizationException {
        String user = accessUtil.getAuthenticatedUser();

        Comment comment = CommentMapper.toEntity(commentDto, new Comment(), taskRepository);

        accessUtil.checkAccess(comment.getTask().getProject().getProjectId(), user);
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        commentRepository.save(comment);

        return CommentMapper.toDTO(comment);
    }

    @Transactional
    public void deleteComment(String id) throws AuthorizationException {
        String loggedInUser = accessUtil.getAuthenticatedUser();
        if (!commentRepository.existsById(id)) {
            return;
        }

        String userId = commentRepository.findById(id).get().getUserId();
        if (userId.equals(loggedInUser)) {
            commentRepository.deleteById(id);
        }
    }

    public List<CommentDto> getCommentsByTask(String id) {
        accessUtil.getAuthenticatedUser();
        List<Comment> comments = commentRepository.findAllByTask_TaskIdOrderByCreatedAtDesc(id);
        return comments.stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
