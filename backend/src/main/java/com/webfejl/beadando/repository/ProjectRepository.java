package com.webfejl.beadando.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webfejl.beadando.entity.Project;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
}
