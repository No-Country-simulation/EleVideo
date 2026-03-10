package com.elevideo.backend.project.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(
        name = "Project.ProjectRequest",
        description = "Parámetros de paginación y ordenamiento para listar proyectos."
)
public record ProjectRequest(

        @Schema(description = "Nombre del proyecto.", example = "Mi campaña de verano")
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
        String name,

        @Schema(description = "Descripción opcional del proyecto.", example = "Videos para la campaña Q3.")
        String description
) {}
