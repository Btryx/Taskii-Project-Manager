package com.webfejl.beadando.repository;

import com.webfejl.beadando.entity.Collaborator;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, String> {

    @Query("SELECT c.user FROM Collaborator c WHERE c.project.projectId = :projectId")
    List<User> findUsersByProjectId(@Param("projectId") String projectId);

    @Query("SELECT c.project FROM Collaborator c WHERE c.user.userId = :userId")
    List<Project> findProjectsByUserId(@Param("userId") String userId);
}
