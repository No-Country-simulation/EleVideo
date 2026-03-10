package com.elevideo.backend.processing.web;

import com.elevideo.backend.processing.api.ProcessingService;
import com.elevideo.backend.processing.api.dto.*;
import com.elevideo.backend.processing.documentation.*;
import com.elevideo.backend.shared.web.ApiResult;
import com.elevideo.backend.shared.web.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/videos/{videoId}")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "05 - Procesamiento", description = "Procesamiento de video y gestión de jobs")
public class ProcessingController {

    private final ProcessingService processingService;

    @CreateJobEndpointDoc
    @PostMapping("/jobs")
    public ResponseEntity<ApiResult<VideoJobCreatedResponse>> createJob(@PathVariable Long projectId, @PathVariable Long videoId, @RequestBody @Valid VideoProcessRequest request, HttpServletRequest httpRequest) {
        String baseUrl = httpRequest.getRequestURL().toString();
        VideoJobCreatedResponse response = processingService.processVideo(videoId, request, baseUrl);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResult.success(response, "Processing job created successfully."));
    }

    @ListJobsEndpointDoc
    @GetMapping("/jobs")
    public ResponseEntity<ApiResult<PageResponse<JobResponse>>> listJobs(@PathVariable Long projectId, @PathVariable Long videoId, @ModelAttribute JobSearchRequest request) {
        PageResponse<JobResponse> response = PageResponse.from(processingService.listJobs(videoId, request));
        return ResponseEntity
                .ok(ApiResult.success(response, "Jobs retrieved successfully."));
    }

    @GetJobsOverviewEndpointDoc
    @GetMapping("/jobs/overview")
    public ResponseEntity<ApiResult<JobOverviewResponse>> getJobsOverview(@PathVariable Long projectId, @PathVariable Long videoId) {
        return ResponseEntity.ok(ApiResult.success(
                processingService.getOverview(videoId), "Job overview retrieved successfully."));
    }

    @GetJobEndpointDoc
    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<ApiResult<JobResponse>> getJob(@PathVariable Long projectId, @PathVariable Long videoId, @PathVariable String jobId) {
        return ResponseEntity.ok(ApiResult.success(
                processingService.getJobStatus(videoId, jobId), "Job retrieved successfully."));
    }


    @CancelJobEndpointDoc
    @PostMapping("/jobs/{jobId}/cancel")
    public ResponseEntity<ApiResult<VideoJobCancelResponse>> cancelJob(@PathVariable Long projectId, @PathVariable Long videoId, @PathVariable String jobId) {
        return ResponseEntity.ok(ApiResult.success(
                processingService.cancelJob(videoId, jobId), "Job cancellation requested successfully."));
    }

    // ----------------------------------------------------------------
    // Renditions
    // ----------------------------------------------------------------

    @ListRenditionsEndpointDoc
    @GetMapping("/renditions")
    public ResponseEntity<ApiResult<PageResponse<VideoRenditionResponse>>> listRenditions(@PathVariable Long projectId, @PathVariable Long videoId, @ModelAttribute VideoRenditionSearchRequest request) {
        PageResponse<VideoRenditionResponse> response = PageResponse.from(
                processingService.getRenditions(videoId, request));
        return ResponseEntity.ok(ApiResult.success(response, "Renditions retrieved successfully."));
    }

    @GetRenditionEndpointDoc
    @GetMapping("/renditions/{renditionId}")
    public ResponseEntity<ApiResult<VideoRenditionResponse>> getRendition(@PathVariable Long projectId, @PathVariable Long videoId, @PathVariable Long renditionId) {
        return ResponseEntity.ok(ApiResult.success(
                processingService.getRenditionById(videoId, renditionId), "Rendition retrieved successfully."));
    }


    @DeleteRenditionEndpointDoc
    @DeleteMapping("/renditions/{renditionId}")
    public ResponseEntity<Void> deleteRendition(@PathVariable Long projectId, @PathVariable Long videoId, @PathVariable Long renditionId) {
        processingService.deleteRendition(videoId, renditionId);
        return ResponseEntity.noContent().build();
    }
}

