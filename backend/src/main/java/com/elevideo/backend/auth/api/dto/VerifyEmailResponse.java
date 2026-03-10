package com.elevideo.backend.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Auth.VerifyEmailResponse")
public record VerifyEmailResponse(

        @Schema(description = "Email verificado", example = "juan.perez@example.com")
        String email
) {}
