package com.elevideo.backend.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(
        name = "Auth.RegisterRequest",
        description = "Datos necesarios para registrar un nuevo usuario.",
        requiredProperties = {"firstName", "lastName", "email", "password"}
)
public record RegisterRequest(

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
        String lastName,

        @Schema(description = "Correo electrónico del usuario.", example = "juan.perez@example.com")
        @Email(message = "El email debe tener un formato válido")
        @NotBlank(message = "El email es requerido")
        String email,

        @Schema(description = "Contraseña del usuario.", example = "P@ssw0rd!")
        @NotBlank(message = "La contraseña es requerida")
        @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
        @Pattern(
                regexp = "^(?=.*[A-ZÑ])(?=.*[a-zñ])(?=.*\\d)(?=.*[-@#$%^&*.,()_+{}|;:'\"<>/!¡¿?])[A-ZÑa-zñ\\d\\W]{6,}$",
                message = "La contraseña debe contener mayúscula, minúscula, número y carácter especial"
        )
        String password
) {}
