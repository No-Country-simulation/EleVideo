package com.elevideo.backend.video.web;

import com.elevideo.backend.shared.web.ApiResult;
import com.elevideo.backend.shared.web.PageResponse;
import com.elevideo.backend.video.api.VideoService;
import com.elevideo.backend.video.api.dto.CreateVideoRequest;
import com.elevideo.backend.video.api.dto.VideoResponse;
import com.elevideo.backend.video.api.dto.VideoSearchRequest;
import com.elevideo.backend.video.documentation.CreateVideoEndpointDoc;
import com.elevideo.backend.video.documentation.DeleteVideoEndpointDoc;
import com.elevideo.backend.video.documentation.GetVideoByIdEndpointDoc;
import com.elevideo.backend.video.documentation.GetVideosEndpointDoc;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/videos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "04 - Videos", description = "Gestión de videos dentro de un proyecto")
public class VideoController {

    private final VideoService videoService;

    @CreateVideoEndpointDoc
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult<VideoResponse>> createVideo(@PathVariable Long projectId, @ModelAttribute @Valid CreateVideoRequest request) {
        VideoResponse response = videoService.createVideo(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(response, "Video uploaded successfully."));
    }

    @GetVideosEndpointDoc
    @GetMapping
    public ResponseEntity<ApiResult<PageResponse<VideoResponse>>> getVideos(@PathVariable Long projectId, @ModelAttribute VideoSearchRequest searchParams) {
        PageResponse<VideoResponse> response = PageResponse.from(videoService.getVideos(projectId, searchParams));
        return ResponseEntity
                .ok(ApiResult.success(response, "Videos retrieved successfully."));
    }

    @GetVideoByIdEndpointDoc
    @GetMapping("/{videoId}")
    public ResponseEntity<ApiResult<VideoResponse>> getVideoById(@PathVariable Long projectId, @PathVariable Long videoId) {
        VideoResponse response = videoService.getVideoById(videoId);
        return ResponseEntity
                .ok(ApiResult.success(response, "Video retrieved successfully."));
    }

    @DeleteVideoEndpointDoc
    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long projectId, @PathVariable Long videoId) {
        videoService.deleteVideo(videoId);
        return ResponseEntity.noContent().build();
    }
}

