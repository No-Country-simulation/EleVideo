package com.elevideo.backend.processing.api.dto;

import com.elevideo.backend.processing.internal.model.JobStatus;
import com.elevideo.backend.processing.internal.model.ProcessingMode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Respuesta al crear un nuevo job de procesamiento.
 * Incluye statusUrl para que el frontend pueda iniciar el polling
 * sin necesidad de construir la URL manualmente.
 */
@Schema(name = "Processing.VideoJobCreatedResponse")
public record VideoJobCreatedResponse(

        @JsonProperty("job_id")
        @Schema(description = "Identificador único del job")
        String jobId,

        @JsonProperty("status")
        @Schema(description = "Estado inicial del job", example = "PENDING")
        JobStatus status,

        @JsonProperty("processing_mode")
        @Schema(description = "Modo de procesamiento solicitado")
        ProcessingMode processingMode,

        @JsonProperty("status_url")
        @Schema(description = "URL para consultar el estado del job (polling)",
                example = "/api/v1/projects/5/videos/12/jobs/abc-123")
        String statusUrl
) {}
