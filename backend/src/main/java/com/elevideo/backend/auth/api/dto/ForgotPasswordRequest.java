package com.elevideo.backend.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "Auth.ForgotPasswordRequest",
        description = "Email para iniciar el proceso de recuperación de contraseña.",
        requiredProperties = {"email"}
)
public record ForgotPasswordRequest(

        @Schema(description = "Correo electrónico del usuario.", example = "juan.perez@example.com")
        @Email(message = "El email debe tener un formato válido")
        @NotBlank(message = "El email es requerido")
        String email
) {}
