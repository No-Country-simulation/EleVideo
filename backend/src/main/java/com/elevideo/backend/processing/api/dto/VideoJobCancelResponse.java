package com.elevideo.backend.processing.api.dto;

import com.elevideo.backend.processing.internal.model.JobStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoJobCancelResponse(
        @JsonProperty("job_id")         String    jobId,
        @JsonProperty("message")        String    message,
        @JsonProperty("previous_status")JobStatus previousStatus
) {}
