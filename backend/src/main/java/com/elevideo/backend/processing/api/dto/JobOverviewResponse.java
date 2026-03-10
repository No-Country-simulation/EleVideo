package com.elevideo.backend.processing.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "Processing.JobOverviewResponse",
        description = "Vista general de jobs de un video: activos y los últimos finalizados")
public record JobOverviewResponse(

        @Schema(description = "Jobs actualmente en ejecución o en cola")
        List<JobResponse> active,

        @Schema(description = "Últimos 10 jobs finalizados (completados, fallidos o cancelados)")
        List<JobResponse> finished
) {}
