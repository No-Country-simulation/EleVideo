package com.elevideo.backend.processing.api.dto;

import com.elevideo.backend.processing.internal.model.BackgroundMode;
import com.elevideo.backend.processing.internal.model.JobStatus;
import com.elevideo.backend.processing.internal.model.Platform;
import com.elevideo.backend.processing.internal.model.ProcessingMode;

import java.time.LocalDateTime;

public record ActiveJobResponse(
        String         jobId,
        JobStatus      status,
        Integer        progress,
        String         phase,
        ProcessingMode processingMode,
        Platform       platform,
        BackgroundMode backgroundMode,
        LocalDateTime  createdAt
) {}
