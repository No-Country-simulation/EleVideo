package com.elevideo.backend.user.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para GET /api/v1/users/me.
 * Devuelve el perfil del usuario autenticado.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Obtener usuario autenticado",
        description = """
        Devuelve el perfil completo del usuario autenticado.

        **Flujo de consulta:**
        1. El usuario envía la solicitud incluyendo su token de autenticación.
        2. El sistema valida el token y obtiene la identidad del usuario.
        3. Se recupera la información del perfil asociada a ese usuario.
        4. La API devuelve los datos completos del perfil.

        **Requiere:** Authorization: Bearer {token}
        """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Datos del usuario obtenidos exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Usuario autenticado",
                                summary = "Respuesta estándar con el perfil del usuario",
                                value = """
                                        {
                                          "success": true,
                                          "message": "User data retrieved successfully.",
                                          "data": {
                                            "id": "6fb1f53e-f1c6-4c87-9337-96062903b8f8",
                                            "firstName": "Juan José",
                                            "lastName": "Pérez Gómez",
                                            "email": "juan.perez@example.com",
                                            "emailVerified": true,
                                            "accountStatus": "ACTIVE"
                                          }
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "❌ Token ausente o inválido",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "No autorizado",
                                summary = "El token JWT no fue enviado o no es válido",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 401,
                                          "error": "UNAUTHORIZED",
                                          "message": "Se requiere autenticación para acceder a este recurso",
                                          "details": [
                                            "Incluye un token JWT válido en el header Authorization: Bearer {token}"
                                          ],
                                          "path": "/api/v1/users/me"
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
                                          "path": "/api/v1/users/me"
                                        }
                                        """
                        )
                )
        )
})
public @interface GetMeEndpointDoc {}
