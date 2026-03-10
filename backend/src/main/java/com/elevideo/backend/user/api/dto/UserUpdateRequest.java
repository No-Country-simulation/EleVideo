package com.elevideo.backend.user.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "User.UserUpdateRequest",
        description = "Datos del usuario para actualizar")
public record UserUpdateRequest(

        @Schema(description = "Nombre(s) del usuario.", example = "Juan José")
        @NotBlank(message = "El nombre es requerido")
        @Pattern(
                regexp = "^[A-Za-zñáéíóúÁÉÍÓÚ]+(?: [A-Za-zñáéíóúÁÉÍÓÚ]+)*$",
                message = "El nombre contiene caracteres no permitidos"
        )
        String firstName,

        @Schema(description = "Apellido(s) del usuario.", example = "Pérez Gómez")
        @NotBlank(message = "El apellido es requerido")
        @Pattern(
                regexp = "^[A-Za-zñáéíóúÁÉÍÓÚ]+(?: [A-Za-zñáéíóúÁÉÍÓÚ]+)*$",
                message = "El apellido contiene caracteres no permitidos"
        )
        String lastName
) {}
