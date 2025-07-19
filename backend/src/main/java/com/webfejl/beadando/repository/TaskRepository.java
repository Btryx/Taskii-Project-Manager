package com.webfejl.beadando.repository;

import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    @Query(value = """
        SELECT * FROM tasks 
        WHERE project_id = :projectId
        AND (:status IS NULL OR task_status = :status)
        AND (:priority IS NULL OR task_priority = :priority)
        """, nativeQuery = true)
    List<Task> filterTasks(
            @Param("projectId") String projectId,
            @Param("status") String status,
            @Param("priority") Integer priority
    );

    @Query(value = "SELECT * FROM tasks WHERE assignee = :userId", nativeQuery = true)
    List<Task> findAllByAssignee(@Param("userId") String userId);

    @Query(value = "SELECT * FROM tasks WHERE task_status = :status", nativeQuery = true)
    List<Task> findAllByStatus(@Param("status") String status);

    @Query(value = "SELECT * FROM tasks WHERE task_status = :status", nativeQuery = true)
    List<Task> findByStatus(@Param("status") String status);

    @Query(value = "SELECT * FROM tasks WHERE task_priority = :priority", nativeQuery = true)
    List<Task> findByPriority(@Param("priority") int priority);

    @Query(value = "SELECT * FROM tasks ORDER BY task_title", nativeQuery = true)
    List<Task> sortByTitle();

    @Query(value = "SELECT * FROM tasks ORDER BY task_date", nativeQuery = true)
    List<Task> sortByDate();

    void deleteAllByProject_ProjectId(String id);
}

