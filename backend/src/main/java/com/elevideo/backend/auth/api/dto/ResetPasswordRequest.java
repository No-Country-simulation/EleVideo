package com.elevideo.backend.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(
        name = "Auth.ResetPasswordRequest",
        description = "Token y nueva contraseña para restablecer el acceso.",
        requiredProperties = {"token", "newPassword"}
)
public record ResetPasswordRequest(

        @Schema(description = "Token JWT enviado al correo electrónico.", example = "eyJhbGciOiJIUzI1NiJ9...")
        @NotBlank(message = "El token es requerido")
        String token,

        @Schema(description = "Nueva contraseña del usuario.", example = "Nu3v0P@ss!")
        @NotBlank(message = "La nueva contraseña es requerida")
        @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
        @Pattern(
                regexp = "^(?=.*[A-ZÑ])(?=.*[a-zñ])(?=.*\\d)(?=.*[-@#$%^&*.,()_+{}|;:'\"<>/!¡¿?])[A-ZÑa-zñ\\d\\W]{6,}$",
                message = "La contraseña debe contener mayúscula, minúscula, número y carácter especial"
        )
        String newPassword
) {}
