package com.elevideo.backend.project.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para GET /api/v1/projects.
 * Devuelve los proyectos del usuario autenticado con paginación.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Listar proyectos",
        description = """
            Devuelve una lista paginada de los proyectos del usuario autenticado.
    
            **Flujo de listado:**
            1. El usuario solicita la lista de proyectos.
            2. El sistema recupera los proyectos asociados al usuario autenticado.
            3. Se aplica paginación y ordenamiento según los parámetros de la solicitud.
            4. La API devuelve la lista paginada de proyectos.
    
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Proyectos obtenidos exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Lista de proyectos",
                                summary = "Respuesta paginada con los proyectos del usuario",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Projects retrieved successfully.",
                                          "data": {
                                            "content": [
                                              {
                                                "id": 1,
                                                "name": "Campaña de verano",
                                                "description": "Videos para la campaña Q3",
                                                "createdAt": "2026-03-08T10:00:00",
                                                "updatedAt": "2026-03-08T10:00:00"
                                              },
                                              {
                                                "id": 2,
                                                "name": "Lanzamiento de producto",
                                                "description": null,
                                                "createdAt": "2026-03-07T09:00:00",
                                                "updatedAt": "2026-03-07T09:00:00"
                                              }
                                            ],
                                            "page": 0,
                                            "size": 20,
                                            "totalElements": 2,
                                            "totalPages": 1,
                                            "hasNext": false,
                                            "hasPrevious": false
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
                                          "path": "/api/v1/projects"
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
                                          "path": "/api/v1/projects"
                                        }
                                        """
                        )
                )
        )
})
public @interface GetProjectsEndpointDoc {}
