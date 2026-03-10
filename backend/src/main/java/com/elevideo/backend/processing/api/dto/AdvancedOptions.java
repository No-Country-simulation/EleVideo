package com.elevideo.backend.processing.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(name = "VideoProcessing.AdvancedOptions",
        description = "Opciones avanzadas para el algoritmo de recorte y composición. Todos opcionales.")
public record AdvancedOptions(

        @Min(0) @Max(1)
        Double headroomRatio,

        @Min(0) @Max(1)
        Double smoothingStrength,

        Integer maxCameraSpeed,
        Boolean applySharpening,
        Boolean useRuleOfThirds,
        Integer edgePadding
) {}
