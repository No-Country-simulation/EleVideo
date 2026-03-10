package com.elevideo.backend.video.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para GET /api/v1/projects/{projectId}/videos.
 * Devuelve los videos de un proyecto con búsqueda, filtrado y paginación.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Listar videos del proyecto",
        description = """
            Devuelve una lista paginada de los videos asociados a un proyecto.
    
            **Flujo de listado:**
            1. El usuario solicita la lista de videos de un proyecto.
            2. El sistema valida que el proyecto pertenezca al usuario autenticado.
            3. Se aplican los filtros y criterios de búsqueda enviados en los parámetros de query.
            4. Los resultados se ordenan y paginan según los parámetros indicados.
            5. La API devuelve la página de videos solicitada.
    
            **Búsqueda y filtros opcionales:**
            - searchTerm — búsqueda parcial por título (case-insensitive).
            - status — filtra por estado del video: UPLOADED, PROCESSING, COMPLETED, FAILED.
    
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Videos obtenidos exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Lista de videos",
                                summary = "Respuesta paginada con los videos del proyecto",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Videos retrieved successfully.",
                                          "data": {
                                            "content": [
                                              {
                                                "id": 42,
                                                "title": "Demo del producto",
                                                "videoUrl": "https://res.cloudinary.com/elevideo/video/upload/v1/Elevideo/xyz.mp4",
                                                "format": "mp4",
                                                "durationInMillis": 95000,
                                                "sizeInBytes": 18350080,
                                                "width": 1920,
                                                "height": 1080,
                                                "status": "UPLOADED",
                                                "projectId": 1,
                                                "createdAt": "2026-03-08T10:00:00",
                                                "updatedAt": "2026-03-08T10:00:00"
                                              }
                                            ],
                                            "page": 0,
                                            "size": 20,
                                            "totalElements": 1,
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
                                          "path": "/api/v1/projects/1/videos"
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
                                          "path": "/api/v1/projects/1/videos"
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
                                          "path": "/api/v1/projects/1/videos"
                                        }
                                        """
                        )
                )
        )
})
public @interface GetVideosEndpointDoc {}
