package com.elevideo.backend.video.api.dto;

import com.elevideo.backend.video.internal.model.VideoStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Respuesta unificada para todos los endpoints de video.
 * Usada tanto al crear como al listar o consultar un video individual.
 * videoUrl es la URL pública para reproducción (storage-agnostic).
 */
@Schema(name = "Video.VideoResponse")
public record VideoResponse(

        @Schema(description = "Identificador del video", example = "12")
        Long id,

        @Schema(description = "Título del video", example = "Mi presentación")
        String title,

        @Schema(description = "URL pública para reproducción del video")
        String videoUrl,

        @Schema(description = "Formato del archivo", example = "mp4")
        String format,

        @Schema(description = "Duración en milisegundos", example = "120000")
        Long durationInSeconds,

        @Schema(description = "Tamaño del archivo en bytes", example = "10485760")
        Long sizeInBytes,

        @Schema(description = "Ancho en píxeles", example = "1920")
        Integer width,

        @Schema(description = "Alto en píxeles", example = "1080")
        Integer height,

        @Schema(description = "Estado del video", example = "UPLOADED")
        VideoStatus status,

        @Schema(description = "ID del proyecto al que pertenece", example = "5")
        Long projectId,

        @Schema(description = "Fecha de creación")
        LocalDateTime createdAt,

        @Schema(description = "Fecha de última actualización")
        LocalDateTime updatedAt
) {}
