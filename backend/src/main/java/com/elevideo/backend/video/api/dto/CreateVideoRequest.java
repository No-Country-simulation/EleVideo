package com.elevideo.backend.video.api.dto;

import com.elevideo.backend.video.internal.validator.VideoFile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

@Schema(
        name = "Video.CreateVideoRequest",
        description = "Datos necesarios para subir un video a un proyecto.",
        requiredProperties = {"title", "video"}
)
public record CreateVideoRequest(

        @Schema(description = "Título del video.", example = "Tutorial React - Hooks")
        @NotBlank(message = "El título es requerido")
        String title,

        @Schema(description = "Archivo de video. Formatos: mp4, mov. Máximo: 200MB.",
                type = "string", format = "binary")
        @NotNull(message = "El archivo de video es requerido")
        @VideoFile
        MultipartFile video
) {}
