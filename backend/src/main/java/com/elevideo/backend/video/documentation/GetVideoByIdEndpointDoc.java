package com.elevideo.backend.video.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para GET /api/v1/projects/{projectId}/videos/{videoId}.
 * Devuelve un video por su ID.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Obtener video por ID",
        description = """
            Devuelve los metadatos de un video específico de un proyecto.
    
            **Flujo de consulta:**
            1. El usuario solicita los metadatos de un video específico.
            2. El sistema valida que el proyecto pertenezca al usuario autenticado.
            3. Se verifica que el video exista y esté asociado al proyecto indicado.
            4. La API devuelve los metadatos del video solicitado.
    
            **Comportamiento:**
            - Devuelve **404 Not Found** si el video no existe o no pertenece al proyecto del usuario autenticado.
    
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Video obtenido exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Video encontrado",
                                summary = "Respuesta estándar con los metadatos del video",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Video retrieved successfully.",
                                          "data": {
                                            "id": 42,
                                            "title": "Demo del producto",
                                            "videoUrl": "https://res.cloudinary.com/elevideo/video/upload/v1/Elevideo/xyz.mp4",
                                            "format": "mp4",
                                            "durationInMillis": 95000,
                                            "sizeInBytes": 18350080,
                                            "width": 1920,
                                            "height": 1080,
                                            "status": "COMPLETED",
                                            "projectId": 1,
                                            "createdAt": "2026-03-08T10:00:00",
                                            "updatedAt": "2026-03-08T11:00:00"
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
                                          "path": "/api/v1/projects/1/videos/42"
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
                                          "path": "/api/v1/projects/1/videos/42"
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
                                          "path": "/api/v1/projects/1/videos/42"
                                        }
                                        """
                        )
                )
        )
})
public @interface GetVideoByIdEndpointDoc {}
