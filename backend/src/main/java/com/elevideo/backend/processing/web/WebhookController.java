package com.elevideo.backend.processing.web;

import com.elevideo.backend.processing.api.dto.webhook.ProcessingJobWebhookRequest;
import com.elevideo.backend.processing.api.dto.webhook.ProgressWebhookRequest;
import com.elevideo.backend.processing.internal.WebhookHandlerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/webhooks")
@Tag(name = "06 - Webhooks", description = "Endpoints internos para recepción de webhooks del microservicio Python")
public class WebhookController {

    private final WebhookHandlerService webhookHandlerService;

    @PostMapping("/job-completed")
    public ResponseEntity<Void> onJobCompleted(@RequestBody @Valid ProcessingJobWebhookRequest request) {
        webhookHandlerService.handleJobCompleted(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/job-progress")
    public ResponseEntity<Void> onProgressUpdate(@RequestBody @Valid ProgressWebhookRequest request) {
        webhookHandlerService.handleProgressUpdate(request);
        return ResponseEntity.ok().build();
    }
}
