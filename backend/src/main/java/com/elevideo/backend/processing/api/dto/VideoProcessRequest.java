package com.elevideo.backend.processing.api.dto;

import com.elevideo.backend.processing.internal.model.BackgroundMode;
import com.elevideo.backend.processing.internal.model.Platform;
import com.elevideo.backend.processing.internal.model.ProcessingMode;
import com.elevideo.backend.processing.internal.model.Quality;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(name = "VideoProcessing.VideoProcessRequest",
        description = "Solicitud unificada para procesar un video.")
public record VideoProcessRequest(

        @NotNull(message = "El processingMode es requerido")
        ProcessingMode processingMode,

        @NotNull(message = "La plataforma es requerida")
        Platform platform,

        @NotNull(message = "La calidad es requerida")
        Quality quality,

        @NotNull(message = "El backgroundMode es requerido")
        BackgroundMode backgroundMode,

        ShortManualOptions shortOptions,

        @Min(5) @Max(60)
        Integer shortAutoDuration,

        AdvancedOptions advancedOptions
) {

    @Schema(name = "VideoProcessing.VideoProcessRequest.ShortManualOptions")
    public record ShortManualOptions(

            @NotNull @Min(0)
            Double startTime,

            @NotNull @Min(5) @Max(60)
            Integer duration
    ) {}
}
