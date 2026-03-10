package com.elevideo.backend.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "Auth.LoginResponse")
public record LoginResponse(

        @Schema(description = "JWT de acceso", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(description = "Datos básicos del usuario autenticado")
        UserSummary user

) {
    @Schema(name = "Auth.LoginResponse.UserSummary")
    public record UserSummary(
            UUID   id,
            String firstName,
            String lastName,
            String email,
            boolean emailVerified
    ) {}
}
