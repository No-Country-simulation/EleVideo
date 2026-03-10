package com.elevideo.backend.processing.api.dto.webhook;

import com.elevideo.backend.processing.internal.model.JobStatus;
import com.elevideo.backend.processing.internal.model.ProcessingMode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Schema(name = "Webhook.ProcessingJobCompletedRequest",
        description = "Payload enviado por Python cuando un job finaliza.")
public record ProcessingJobWebhookRequest(

        @NotBlank @JsonProperty("job_id")
        String jobId,

        @NotNull @JsonProperty("status")
        JobStatus status,

        @NotNull @JsonProperty("processing_mode")
        ProcessingMode processingMode,

        @JsonProperty("elapsed_seconds")   Double  elapsedSeconds,
        @JsonProperty("phase")             String  phase,
        @NotNull @JsonProperty("completed_at") Instant completedAt,
        @JsonProperty("output_url")        String  outputUrl,
        @JsonProperty("thumbnail_url")     String  thumbnailUrl,
        @JsonProperty("preview_url")       String  previewUrl,
        @JsonProperty("quality_score")     Double  qualityScore,
        @JsonProperty("output_duration_seconds") Double outputDurationSeconds,
        @JsonProperty("segment_start")     Double  segmentStart,
        @JsonProperty("segment_duration")  Integer segmentDuration,
        @JsonProperty("error_detail")      String  errorDetail
) {
    public boolean isCompleted() { return status == JobStatus.COMPLETED; }
    public boolean isFailed()    { return status == JobStatus.FAILED;    }
}
