package com.webfejl.beadando.repository;

import com.webfejl.beadando.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findAllByTask_TaskIdOrderByCreatedAtDesc(String id);
}
