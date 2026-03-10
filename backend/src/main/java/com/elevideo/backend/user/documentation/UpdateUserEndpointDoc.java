package com.elevideo.backend.user.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para PATCH /api/v1/users/me.
 * Actualiza el nombre y apellido del usuario autenticado.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Actualizar perfil de usuario",
        description = """
            Actualiza el nombre y apellido del usuario autenticado.
    
            **Flujo de actualización:**
            1. El usuario envía los nuevos valores para su perfil.
            2. El sistema valida la solicitud del usuario autenticado.
            3. Si los datos son válidos, se actualizan los campos **name** y **lastName**.
            4. La API devuelve la información actualizada del perfil del usuario.
    
            **Restricciones:**
            - El campo **email** se gestiona mediante un flujo separado y no puede modificarse aqui.
            - La contraseña se actualiza exclusivamente mediante el endpoint PATCH /api/v1/users/me/password.
    
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Perfil actualizado exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Actualización exitosa",
                                summary = "Respuesta con el perfil actualizado",
                                value = """
                                        {
                                          "success": true,
                                          "message": "User updated successfully.",
                                          "data": {
                                            "id": "6fb1f53e-f1c6-4c87-9337-96062903b8f8",
                                            "firstName": "Carlos",
                                            "lastName": "García López",
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
                responseCode = "400",
                description = "❌ Error de validación",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Campos inválidos",
                                summary = "Nombre o apellido vacíos o con caracteres no permitidos",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 400,
                                          "error": "VALIDATION_ERROR",
                                          "message": "Error de validación en los campos",
                                          "fieldErrors": {
                                            "firstName": "no debe estar vacío",
                                            "lastName": "el apellido contiene caracteres no permitidos"
                                          },
                                          "path": "/api/v1/users/me"
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
public @interface UpdateUserEndpointDoc {}
