package com.elevideo.backend.processing.api.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Webhook.ProgressWebhookRequest",
        description = "Payload enviado por Python que actualiza el proceso del job en tiempo real.")
public class ProgressWebhookRequest {

    @JsonProperty("job_id")
    private String jobId;

    private String  status;
    private Integer progress;
    private String  phase;

    @JsonProperty("eta_seconds")
    private Integer etaSeconds;

    @JsonProperty("elapsed_seconds")
    private Double elapsedSeconds;

    private String message;
}
