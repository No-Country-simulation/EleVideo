package com.elevideo.backend.auth.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para POST /api/v1/auth/reset-password.
 * Restablece la contraseña usando el token recibido por correo.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Restablecer contraseña",
        description = """
            Establece una nueva contraseña utilizando el token de restablecimiento enviado al correo del usuario.
    
            **Flujo de restablecimiento:**
            1. El usuario envía el token de restablecimiento junto con su nueva contraseña.
            2. El sistema valida el token (existencia, expiración y uso único).
            3. Si el token es válido, la contraseña del usuario se actualiza.
            4. El token queda invalidado para evitar reutilización.
            5. El usuario puede iniciar sesión nuevamente con la nueva contraseña.
    
            **Nota:**
            - Tras el restablecimiento exitoso, las sesiones activas no se invalidan automáticamente.
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Contraseña restablecida exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Restablecimiento exitoso",
                                summary = "La contraseña fue actualizada correctamente",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Password reset successfully.",
                                          "data": null
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "❌ Error de validación",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Campos inválidos",
                                summary = "La nueva contraseña no cumple los requisitos de seguridad",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 400,
                                          "error": "VALIDATION_ERROR",
                                          "message": "Error de validación en los campos",
                                          "fieldErrors": {
                                            "token": "no debe estar vacío",
                                            "newPassword": "debe contener mayúscula, minúscula, número y carácter especial"
                                          },
                                          "path": "/api/v1/auth/reset-password"
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "❌ Token inválido o expirado",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Token inválido",
                                summary = "El token de restablecimiento no es válido o ya fue utilizado",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 401,
                                          "error": "TOKEN_INVALID",
                                          "message": "El token de restablecimiento no es válido o ha expirado",
                                          "details": [
                                            "Solicita un nuevo enlace de restablecimiento de contraseña"
                                          ],
                                          "path": "/api/v1/auth/reset-password"
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "❌ Error interno del servidor",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Error interno",
                                summary = "Error inesperado en el servidor",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 500,
                                          "error": "INTERNAL_SERVER_ERROR",
                                          "message": "Error interno del servidor",
                                          "details": [
                                            "Ocurrió un error inesperado. Por favor, intenta más tarde"
                                          ],
                                          "path": "/api/v1/auth/reset-password"
                                        }
                                        """
                        )
                )
        )
})
public @interface ResetPasswordEndpointDoc {}
