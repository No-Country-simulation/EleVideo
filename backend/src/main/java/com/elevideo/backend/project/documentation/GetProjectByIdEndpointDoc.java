package com.elevideo.backend.project.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para GET /api/v1/projects/{projectId}.
 * Devuelve un proyecto por su ID.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Obtener proyecto por ID",
        description = """
            Devuelve un proyecto específico del usuario autenticado.
    
            **Flujo de consulta:**
            1. El usuario solicita un proyecto específico mediante su identificador.
            2. El sistema verifica que el proyecto exista y pertenezca al usuario autenticado.
            3. Si el proyecto es válido, se recupera su información.
            4. La API devuelve los datos completos del proyecto solicitado.
    
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Proyecto obtenido exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Proyecto encontrado",
                                summary = "Respuesta estándar con los datos del proyecto",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Project retrieved successfully.",
                                          "data": {
                                            "id": 1,
                                            "name": "Campaña de verano",
                                            "description": "Videos para la campaña Q3",
                                            "videoCount": 3
                                            "createdAt": "2026-03-08T10:00:00",
                                            "updatedAt": "2026-03-08T10:00:00"
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
public @interface GetProjectByIdEndpointDoc {}
