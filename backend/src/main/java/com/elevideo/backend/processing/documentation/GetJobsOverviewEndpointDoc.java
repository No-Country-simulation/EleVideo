package com.elevideo.backend.processing.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para GET /api/v1/projects/{projectId}/videos/{videoId}/jobs/overview.
 * Vista rápida de jobs activos y los últimos finalizados del video.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Resumen de jobs del video",
        description = """
            Devuelve una vista resumida de los jobs asociados a un video.
            
            **Flujo de consulta:**
            1. El usuario solicita el resumen de jobs para un video específico.
            2. El sistema valida que el video pertenezca al usuario autenticado.
            3. Se recuperan los jobs activos del video.
            4. Se recuperan los últimos jobs finalizados.
            5. La API devuelve ambos conjuntos en una sola respuesta.
            
            **Contenido de la respuesta:**
            - active — jobs en estado **PENDING** o **PROCESSING** (máximo 50).
            - finished — últimos **10 jobs** en estado **COMPLETED**, **FAILED** o **CANCELLED**.
            
            **Nota:**
            - Este endpoint está diseñado para la vista principal del **dashboard**.
            - Permite evitar realizar múltiples solicitudes para obtener jobs activos y finalizados.
            
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Resumen de jobs obtenido exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Resumen de jobs",
                                summary = "Respuesta con jobs activos y últimos finalizados",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Job overview retrieved successfully.",
                                          "data": {
                                            "active": [
                                              {
                                                "jobId": "b5a1d3e2-0f48-4c7b-ad23-8e4f6g01b3c4",
                                                "status": "PROCESSING",
                                                "progress": 62,
                                                "phase": "encoding",
                                                "processingMode": "FULL",
                                                "platform": "YOUTUBE",
                                                "backgroundMode": "KEEP",
                                                "quality": "HIGH"
                                                "output": null,
                                                "errorDetail": null,
                                                "createdAt": "2026-03-08T09:50:00"
                                              }
                                            ],
                                            "finished": [
                                              {
                                                "jobId": "a3f8c2d1-9e47-4b6a-bc12-7d3e5f90a1b2",
                                                "status": "COMPLETED",
                                                "progress": 100,
                                                "phase": "finished",
                                                "processingMode": "SHORT_AUTO",
                                                "platform": "INSTAGRAM",
                                                "backgroundMode": "BLUR",
                                                "output": {
                                                  "videoUrl": "https://res.cloudinary.com/elevideo/video/upload/processed/abc.mp4",
                                                  "thumbnailUrl": "https://res.cloudinary.com/elevideo/image/upload/thumb/abc.jpg",
                                                  "previewUrl": "https://res.cloudinary.com/elevideo/video/upload/preview/abc.mp4"
                                                },
                                                "errorDetail": null,
                                                "createdAt": "2026-03-08T09:00:00"
                                              }
                                            ]
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
                                          "path": "/api/v1/projects/1/videos/42/jobs/overview"
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
                                          "path": "/api/v1/projects/1/videos/42/jobs/overview"
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
                                          "path": "/api/v1/projects/1/videos/42/jobs/overview"
                                        }
                                        """
                        )
                )
        )
})
public @interface GetJobsOverviewEndpointDoc {}
