package com.elevideo.backend.user.api.dto;

import com.elevideo.backend.user.internal.model.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "User.AuthenticatedUserResponse",
        description = "Datos del usuario autenticado actualmente")
public record AuthenticatedUserResponse(

        @Schema(description = "Identificador único del usuario")
        UUID id,

        @Schema(description = "Nombre(s) del usuario", example = "Juan José")
        String firstName,

        @Schema(description = "Apellido(s) del usuario", example = "Pérez Gómez")
        String lastName,

        @Schema(description = "Correo electrónico", example = "juan.perez@example.com")
        String email,

        @Schema(description = "Indica si el email fue verificado", example = "true")
        boolean emailVerified,

        @Schema(description = "Estado de la cuenta", example = "ACTIVE")
        AccountStatus accountStatus
) {}
