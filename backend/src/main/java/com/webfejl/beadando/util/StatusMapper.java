package com.webfejl.beadando.util;

import com.webfejl.beadando.dto.StatusDto;
import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.Status;
import com.webfejl.beadando.exception.ProjectNotFoundException;
import com.webfejl.beadando.repository.ProjectRepository;

public class StatusMapper {

    public static StatusDto toDTO(Status status) {
        return new StatusDto(
                status.getStatusId(),
                status.getStatusName(),
                status.getOrderNumber(),
                status.getProject() != null ? status.getProject().getProjectId() : null
        );
    }

    public static Status toEntity(StatusDto statusDto, Status status, ProjectRepository projectRepository) {
        status.setStatusId(statusDto.statusId());
        status.setStatusName(statusDto.statusName());
        status.setOrderNumber(status.getOrderNumber());
        Project project = projectRepository.findById(statusDto.projectId()).orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        status.setProject(project);
        return status;
    }
}