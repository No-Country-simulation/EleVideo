package com.elevideo.backend.project.web;

import com.elevideo.backend.project.api.ProjectService;
import com.elevideo.backend.project.api.dto.ProjectPageableRequest;
import com.elevideo.backend.project.api.dto.ProjectRequest;
import com.elevideo.backend.project.api.dto.ProjectResponse;
import com.elevideo.backend.project.documentation.*;
import com.elevideo.backend.shared.web.ApiResult;
import com.elevideo.backend.shared.web.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "03 - Proyectos", description = "Gestión de proyectos del usuario")
public class ProjectController {

    private final ProjectService projectService;

    @CreateProjectEndpointDoc
    @PostMapping
    public ResponseEntity<ApiResult<ProjectResponse>> create(@RequestBody @Valid ProjectRequest request) {
        ProjectResponse response = projectService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(response, "Project created successfully."));
    }

    @GetProjectsEndpointDoc
    @GetMapping
    public ResponseEntity<ApiResult<PageResponse<ProjectResponse>>> getProjects(@ModelAttribute ProjectPageableRequest pageable) {
        PageResponse<ProjectResponse> response = PageResponse.from(projectService.getProjectsByUser(pageable));
        return ResponseEntity.ok(ApiResult.success(response, "Projects retrieved successfully."));
    }

    @GetProjectByIdEndpointDoc
    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResult<ProjectResponse>> getById(@PathVariable Long projectId) {
        ProjectResponse response = projectService.getById(projectId);
        return ResponseEntity.ok(ApiResult.success(response, "Project retrieved successfully."));
    }

    @UpdateProjectEndpointDoc
    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResult<ProjectResponse>> update(@PathVariable Long projectId, @RequestBody @Valid ProjectRequest request) {
        ProjectResponse response = projectService.update(projectId, request);
        return ResponseEntity.ok(ApiResult.success(response, "Project updated successfully."));
    }

    @DeleteProjectEndpointDoc
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> delete(@PathVariable Long projectId) {
        projectService.delete(projectId);
        return ResponseEntity.noContent().build();
    }
}

