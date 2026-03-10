package com.elevideo.backend.processing.api.dto;

import com.elevideo.backend.processing.internal.model.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Respuesta unificada para todos los endpoints de jobs.
 * Reemplaza JobResponse y ActiveJobResponse — el frontend
 * maneja un único modelo independientemente del endpoint consultado.
 *
 * output y errorDetail son null hasta que el job finaliza.
 */
@Schema(name = "Processing.JobResponse")
public record JobResponse(

        @Schema(description = "Identificador único del job en el servicio de procesamiento")
        String jobId,

        @Schema(description = "Estado actual del job", example = "PROCESSING")
        JobStatus status,

        @Schema(description = "Porcentaje de progreso (0-100)", example = "45")
        Integer progress,

        @Schema(description = "Fase actual del procesamiento", example = "encoding")
        String phase,

        @Schema(description = "Modo de procesamiento aplicado")
        ProcessingMode processingMode,

        @Schema(description = "Plataforma de destino")
        Platform platform,

        @Schema(description = "Modo de background aplicado")
        BackgroundMode backgroundMode,

        @Schema(description = "Calidad de procesamiento aplicada")
        Quality quality,

        @Schema(description = "URLs de salida. Disponibles solo cuando status=COMPLETED.")
        JobOutput output,

        @Schema(description = "Detalle del error. Disponible solo cuando status=FAILED.")
        String errorDetail,

        @Schema(description = "Fecha de creación del job")
        LocalDateTime createdAt

) {
    @Schema(name = "Processing.JobResponse.JobOutput")
    public record JobOutput(
            @Schema(description = "URL del video procesado")
            String videoUrl,

            @Schema(description = "URL de la miniatura")
            String thumbnailUrl,

            @Schema(description = "URL del preview corto")
            String previewUrl
    ) {}
}
