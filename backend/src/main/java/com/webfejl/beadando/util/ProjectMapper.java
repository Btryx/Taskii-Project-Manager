package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.ProjectDto;
import com.webfejl.beadando.entity.Project;

public class ProjectMapper {

    public static ProjectDto toDTO(Project project) {
        return new ProjectDto(
                project.getProjectId(),
                project.getProjectName(),
                project.getProjectDesc(),
                project.getCreatedAt(),
                project.getActive(),
                project.getParentId(),
                project.getUserId()
        );
    }

    public static Project toEntity(ProjectDto projectDTO, Project project) {
        project.setProjectId(projectDTO.projectId());
        project.setProjectName(projectDTO.projectName());
        project.setActive(projectDTO.active());
        project.setCreatedAt(projectDTO.createdAt());
        project.setParentId(projectDTO.parentId());
        project.setProjectDesc(projectDTO.projectDesc());
        project.setUserId(projectDTO.userId());

        return project;
    }
}
