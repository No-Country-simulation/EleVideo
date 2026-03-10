package com.elevideo.backend.auth.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para POST /api/v1/auth/verify-email.
 * Verifica el email del usuario con el token recibido por correo.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Verificar email",
        description = """
            Activa la cuenta del usuario utilizando el token de verificación enviado a su correo electrónico.
    
            **Flujo de verificación:**
            1. El usuario hace clic en el enlace de verificación recibido por email.
            2. El frontend extrae el token del enlace y llama a este endpoint.
            3. El sistema valida el token de verificación.
            4. Si el token es válido, la cuenta del usuario se marca como verificada.
            5. La API devuelve información básica del usuario con el email verificado.
    
            **Nota:**
            - El token es de un solo uso y expira tras el período configurado en el servidor.
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Email verificado exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Verificación exitosa",
                                summary = "Respuesta estándar de verificación exitosa",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Email verified successfully.",
                                          "data": {
                                            "email": "juan.perez@example.com"
                                          }
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
                                name = "Token ausente",
                                summary = "Cuando el token no fue enviado en el cuerpo",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 400,
                                          "error": "VALIDATION_ERROR",
                                          "message": "Error de validación en los campos",
                                          "fieldErrors": {
                                            "token": "no debe estar vacío"
                                          },
                                          "path": "/api/v1/auth/verify-email"
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
                                summary = "El token no es válido, ya fue usado o ha expirado",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 401,
                                          "error": "TOKEN_INVALID",
                                          "message": "El token de verificación no es válido o ha expirado",
                                          "details": [
                                            "Solicita un nuevo correo de verificación"
                                          ],
                                          "path": "/api/v1/auth/verify-email"
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
                                          "path": "/api/v1/auth/verify-email"
                                        }
                                        """
                        )
                )
        )
})
public @interface VerifyEmailEndpointDoc {}
