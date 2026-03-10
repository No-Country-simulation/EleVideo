package com.elevideo.backend.project.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para PUT /api/v1/projects/{projectId}.
 * Reemplaza todos los campos de un proyecto existente.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Actualizar proyecto",
        description = """
        Reemplaza completamente los campos de un proyecto existente (semántica PUT).

        **Flujo de actualización:**
        1. El usuario envía los nuevos datos del proyecto en el cuerpo de la solicitud.
        2. El sistema valida que el proyecto exista y pertenezca al usuario autenticado.
        3. Si los datos son válidos, se reemplazan completamente los campos del proyecto.
        4. La API devuelve la información actualizada del proyecto.

        **Requiere:** Authorization: Bearer {token}
        """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Proyecto actualizado exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Proyecto actualizado",
                                summary = "Respuesta con los datos actualizados del proyecto",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Project updated successfully.",
                                          "data": {
                                            "id": 1,
                                            "name": "Campaña de invierno",
                                            "description": "Videos actualizados para Q4",
                                            "createdAt": "2026-03-08T10:00:00",
                                            "updatedAt": "2026-03-08T12:00:00"
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
                                summary = "El nombre del proyecto es obligatorio",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 400,
                                          "error": "VALIDATION_ERROR",
                                          "message": "Error de validación en los campos",
                                          "fieldErrors": {
                                            "name": "no debe estar vacío"
                                          },
                                          "path": "/api/v1/projects/1"
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
                                          "path": "/api/v1/projects/1"
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "❌ Proyecto no encontrado",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "No encontrado",
                                summary = "El proyecto no existe o no pertenece al usuario autenticado",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 404,
                                          "error": "PROJECT_NOT_FOUND",
                                          "message": "Proyecto no encontrado con id: 1",
                                          "details": [
                                            "Verifica que el ID del proyecto sea correcto y te pertenezca"
                                          ],
                                          "path": "/api/v1/projects/1"
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
                                          "path": "/api/v1/projects/1"
                                        }
                                        """
                        )
                )
        )
})
public @interface UpdateProjectEndpointDoc {}
