package com.webfejl.beadando.repository;

import com.webfejl.beadando.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StatusRepository extends JpaRepository<Status, String> {

    @Query("SELECT s FROM Status s WHERE s.project.projectId = :projectId ORDER BY orderNumber")
    List<Status> findByProjectId(@Param("projectId") String projectId);
}