package com.elevideo.backend.processing.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para GET /api/v1/projects/{projectId}/videos/{videoId}/jobs.
 * Devuelve los jobs de procesamiento de un video con filtros y paginación.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Listar jobs del video",
        description = """
            Devuelve una lista paginada de jobs de procesamiento asociados a un video.
            
            **Flujo de listado:**
            1. El usuario solicita la lista de jobs de procesamiento para un video.
            2. El sistema valida que el video pertenezca al usuario autenticado.
            3. Se aplican los filtros opcionales enviados en los parámetros de query.
            4. Los resultados se ordenan y paginan según los parámetros indicados.
            5. La API devuelve la página de jobs solicitada.
            
            **Filtros opcionales:**
            - status — filtra por estado del job (multi-valor): PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED
            - processingMode — filtra por modo de procesamiento: SHORT_AUTO, SHORT_MANUAL, FULL
            - platform — filtra por plataforma destino: INSTAGRAM, TIKTOK, YOUTUBE, etc.
            - backgroundMode — filtra por modo de fondo: BLUR, REMOVE, KEEP
            
            **Parámetros de paginación:**
            - page — número de página (base 0)
            - size — cantidad de elementos por página
            - sortBy — campo de ordenamiento
            - sortDirection — dirección del orden (ASC o DESC)
            
            **Valores por defecto:**
            - ordenamiento: createdAt DESC
            - tamaño de página: 10
            
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Jobs obtenidos exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Lista de jobs",
                                summary = "Respuesta paginada con los jobs del video",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Jobs retrieved successfully.",
                                          "data": {
                                            "content": [
                                              {
                                                "jobId": "a3f8c2d1-9e47-4b6a-bc12-7d3e5f90a1b2",
                                                "status": "COMPLETED",
                                                "progress": 100,
                                                "phase": "finished",
                                                "processingMode": "SHORT_AUTO",
                                                "platform": "INSTAGRAM",
                                                "backgroundMode": "BLURRED",
                                                "quality": "HIGH"
                                                "output": {
                                                  "videoUrl": "https://res.cloudinary.com/elevideo/video/upload/processed/abc.mp4",
                                                  "thumbnailUrl": "https://res.cloudinary.com/elevideo/image/upload/thumb/abc.jpg",
                                                  "previewUrl": "https://res.cloudinary.com/elevideo/video/upload/preview/abc.mp4"
                                                },
                                                "errorDetail": null,
                                                "createdAt": "2026-03-08T10:00:00"
                                              }
                                            ],
                                            "page": 0,
                                            "size": 10,
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
                                          "path": "/api/v1/projects/1/videos/42/jobs"
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "❌ Video no encontrado",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "No encontrado",
                                summary = "El video no existe o no pertenece al proyecto del usuario",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 404,
                                          "error": "VIDEO_NOT_FOUND",
                                          "message": "Video no encontrado con id: 42",
                                          "details": [
                                            "Verifica que el ID del video sea correcto y pertenezca al proyecto indicado"
                                          ],
                                          "path": "/api/v1/projects/1/videos/42/jobs"
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
                                          "path": "/api/v1/projects/1/videos/42/jobs"
                                        }
                                        """
                        )
                )
        )
})
public @interface ListJobsEndpointDoc {}
