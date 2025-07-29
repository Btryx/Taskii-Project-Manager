package com.webfejl.beadando.repository;

import com.webfejl.beadando.entity.Collaborator;
import com.webfejl.beadando.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, String> {

    @Query("SELECT c.userId FROM Collaborator c WHERE c.project.projectId = :projectId")
    List<String> findUsersByProjectId(@Param("projectId") String projectId);

    @Query("SELECT c FROM Collaborator c WHERE c.project.projectId = :projectId")
    List<Collaborator> findAllByProjectId(@Param("projectId") String projectId);

    @Query("SELECT c.project FROM Collaborator c WHERE c.userId = :userId")
    List<Project> findProjectsByUserId(@Param("userId") String userId);

    @Query("SELECT COUNT(c) > 0 FROM Collaborator c WHERE c.userId = :userId AND c.project.projectId = :projectId AND c.role = :role")
    boolean isUserAdminOnProject(@Param("userId") String userId, @Param("projectId") String projectId, @Param("role") String role);

    boolean existsByProject_ProjectIdAndUserId(String projectId, String userId);

    void deleteAllByProject_ProjectId(String projectId);
}
