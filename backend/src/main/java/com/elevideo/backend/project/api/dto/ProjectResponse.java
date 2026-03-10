package com.elevideo.backend.project.api.dto;

import java.time.LocalDateTime;

public record ProjectResponse(
        Long          id,
        String        name,
        String        description,
        long          videoCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
