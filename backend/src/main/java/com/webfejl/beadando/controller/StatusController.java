package com.webfejl.beadando.controller;


import com.webfejl.beadando.dto.StatusDto;
import com.webfejl.beadando.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/statuses")
@CrossOrigin(origins = "http://localhost:4200")
public class StatusController {

    private final StatusService service;

    @Autowired
    public StatusController(StatusService service) {
        this.service = service;
    }

    @GetMapping("{projectId}/all")
    public ResponseEntity<?> getAllProjectStatuses(@PathVariable String projectId) {
        return ResponseEntity.ok(service.getStatusesForProject(projectId));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createStatus(@RequestBody StatusDto statusDto) {
        return ResponseEntity.ok(service.createStatus(statusDto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteStatus(@PathVariable String id) {
        service.deleteStatus(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/create")
    public ResponseEntity<?> updateStatus(
            @PathVariable String id,
            @RequestBody StatusDto statusDto) {
        return ResponseEntity.ok(service.updateStatus(statusDto, id));
    }
}
