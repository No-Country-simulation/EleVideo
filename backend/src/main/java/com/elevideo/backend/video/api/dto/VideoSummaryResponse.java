package com.elevideo.backend.video.api.dto;

import com.elevideo.backend.video.internal.model.VideoStatus;

import java.time.LocalDateTime;

public record VideoSummaryResponse(
        Long          id,
        String        title,
        String        secureUrl,
        String        format,
        Long          durationInSeconds,
        Long          sizeInBytes,
        Integer       width,
        Integer       height,
        VideoStatus   status,
        Long          projectId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
