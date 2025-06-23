package com.webfejl.beadando.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webfejl.beadando.entity.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    @Query("SELECT p FROM Project p WHERE p.user.userId = :userId")
    List<Project> findByUserId(@Param("userId") String userId);

    boolean existsByProjectIdAndUser_UserId(String projectId, String userId);
}
