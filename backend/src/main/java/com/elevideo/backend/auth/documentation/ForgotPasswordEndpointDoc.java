package com.elevideo.backend.auth.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para POST /api/v1/auth/forgot-password.
 * Inicia el proceso de recuperación de contraseña.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Recuperar contraseña",
        description = """
            Envía un enlace de restablecimiento de contraseña al email proporcionado.
    
            **Flujo de restablecimiento:**
            1. El usuario envía su dirección de email registrada.
            2. El sistema verifica si existe una cuenta asociada a ese email.
            3. Si existe, se genera un token temporal de restablecimiento.
            4. Se envía al usuario un enlace con instrucciones para definir una nueva contraseña.
    
            **Comportamiento de la respuesta:**
            - La API devuelve siempre **200 OK**, independientemente de si el email existe.
    
            **Nota de seguridad:**
            - La respuesta es intencionalmente ambigua para evitar la enumeración de usuarios registrados en el sistema.
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Solicitud procesada (se envía el correo si el email existe)",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Solicitud procesada",
                                summary = "Respuesta estándar, independiente de si el email existe",
                                value = """
                                        {
                                          "success": true,
                                          "message": "If that email is registered, you will receive a password reset link shortly.",
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
                                name = "Email inválido",
                                summary = "El email enviado no tiene formato válido",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 400,
                                          "error": "VALIDATION_ERROR",
                                          "message": "Error de validación en los campos",
                                          "fieldErrors": {
                                            "email": "debe ser una dirección de correo válida"
                                          },
                                          "path": "/api/v1/auth/forgot-password"
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
                                          "path": "/api/v1/auth/forgot-password"
                                        }
                                        """
                        )
                )
        )
})
public @interface ForgotPasswordEndpointDoc {}
