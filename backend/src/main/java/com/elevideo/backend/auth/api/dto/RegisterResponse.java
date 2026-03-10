package com.elevideo.backend.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Auth.RegisterResponse")
public record RegisterResponse(

        @Schema(description = "Email al que se envió el correo de verificación",
                example = "juan.perez@example.com")
        String email
) {}
