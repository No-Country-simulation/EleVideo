package com.elevideo.backend.processing.internal.client;

import com.elevideo.backend.processing.api.dto.AdvancedOptions;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Payload enviado al microservicio Python para iniciar el procesamiento.
 * Solo visible dentro de processing/internal/client.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoPythonRequest {

    private String platform;
    private String quality;

    @JsonProperty("background_mode")
    private String backgroundMode;

    @JsonProperty("processing_mode")
    private String processingMode;

    @JsonProperty("cloudinary_input_url")
    private String cloudinaryInputUrl;

    @JsonProperty("short_options")
    private ShortOptionsDto shortOptions;

    @JsonProperty("short_auto_duration")
    private Integer shortAutoDuration;

    @JsonProperty("advanced_options")
    private AdvancedOptions advancedOptions;

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ShortOptionsDto {

        @JsonProperty("start_time")
        private Double startTime;

        private Integer duration;
    }
}
