package com.elevideo.backend.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "Auth.VerifyEmailRequest",
        description = "Token JWT para verificar la cuenta del usuario.",
        requiredProperties = {"token"}
)
public record VerifyEmailRequest(

        @Schema(description = "Token JWT de verificación enviado por correo.", example = "eyJhbGciOiJIUzI1NiJ9...")
        @NotBlank(message = "El token de verificación es obligatorio")
        String token
) {}
