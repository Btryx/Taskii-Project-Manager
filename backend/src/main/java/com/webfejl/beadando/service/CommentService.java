package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.CommentDto;
import com.webfejl.beadando.entity.Comment;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.repository.CommentRepository;
import com.webfejl.beadando.repository.TaskRepository;
import com.webfejl.beadando.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    public CommentService(AccessUtil accessUtil, UserRepository userRepository, TaskRepository taskRepository, CommentRepository commentRepository) {
        this.accessUtil = accessUtil;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
    }


    @Transactional
    public CommentDto createComment(CommentDto commentDto) throws AuthorizationException {
        User user = accessUtil.getAuthenticatedUser();

        Comment comment = CommentMapper.toEntity(commentDto, new Comment(), userRepository, taskRepository);

        accessUtil.checkAccess(comment.getTask().getProject().getProjectId(), user);
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        commentRepository.save(comment);

        return CommentMapper.toDTO(comment);
    }

    @Transactional
    public void deleteComment(String id) throws AuthorizationException {
        User loggedInUser = accessUtil.getAuthenticatedUser();
        if (!commentRepository.existsById(id)) {
            return;
        }

        User user = commentRepository.findById(id).get().getUser();
        if (user.getUserId().equals(loggedInUser.getUserId())) {
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
