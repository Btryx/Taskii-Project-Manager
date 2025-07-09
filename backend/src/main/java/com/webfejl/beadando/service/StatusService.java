package com.webfejl.beadando.service;

import com.webfejl.beadando.dto.StatusDto;
import com.webfejl.beadando.entity.Status;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.AuthorizationException;
import com.webfejl.beadando.exception.ProjectNotFoundException;
import com.webfejl.beadando.repository.ProjectRepository;
import com.webfejl.beadando.repository.StatusRepository;
import com.webfejl.beadando.util.AccessUtil;
import com.webfejl.beadando.util.StatusMapper;
import jakarta.transaction.Transactional;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StatusService {

    private final StatusRepository statusRepository;
    private final AccessUtil accessUtil;
    private final ProjectRepository projectRepository;

    public StatusService(StatusRepository statusRepository, AccessUtil accessUtil, ProjectRepository projectRepository) {
        this.statusRepository = statusRepository;
        this.accessUtil = accessUtil;
        this.projectRepository = projectRepository;
    }

    public List<StatusDto> getStatusesForProject(String projectId) throws AuthenticationException {
        User user = accessUtil.getAuthenticatedUser();
        accessUtil.checkAccess(projectId, user);
        return statusRepository.findByProjectId(projectId).stream().map(StatusMapper::toDTO).toList();
    }

    @Transactional
    public StatusDto createStatus(StatusDto statusDto) throws AuthenticationException {
        User user = accessUtil.getAuthenticatedUser();
        Status status = StatusMapper.toEntity(statusDto, new Status(), projectRepository);

        accessUtil.checkAccess(status.getProject().getProjectId(), user);

        Status savedStatus = statusRepository.save(status);
        return StatusMapper.toDTO(savedStatus);
    }

    @Transactional
    public StatusDto updateStatus(StatusDto statusDto, String id) throws AuthenticationException {
        User user = accessUtil.getAuthenticatedUser();
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Status not found with ID: " + id));

        accessUtil.checkAccess(status.getProject().getProjectId(), user);

        Status newStatus = StatusMapper.toEntity(statusDto, status, projectRepository);

        return StatusMapper.toDTO(statusRepository.save(newStatus));
    }

    @Transactional
    public void deleteStatus(String id) throws AuthorizationException, ProjectNotFoundException {
        User user = accessUtil.getAuthenticatedUser();
        Status status = statusRepository.findById(id).get();
        if (!projectRepository.existsById(status.getProject().getProjectId())) {
            throw new ProjectNotFoundException("Project not found with ID: " + id);
        }
        accessUtil.checkAccess(status.getProject().getProjectId(), user);

        statusRepository.deleteById(id);
    }
}
