package com.elevideo.backend.processing.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para GET /api/v1/projects/{projectId}/videos/{videoId}/renditions/{renditionId}.
 * Devuelve una rendition por su ID.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Obtener rendition por ID",
        description = """
        Devuelve una rendition específica de un video.

        **Flujo de consulta:**
        1. El usuario solicita una rendition específica asociada a un video.
        2. El sistema valida que el video pertenezca al usuario autenticado.
        3. Se verifica que la rendition exista y esté asociada al video indicado.
        4. La API devuelve la información de la rendition solicitada.

        **Comportamiento:**
        - Devuelve **404 Not Found** si la rendition no existe o no pertenece al video indicado.

        **Requiere:** Authorization: Bearer {token}
        """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Rendition obtenida exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Rendition encontrada",
                                summary = "Respuesta estándar con los datos de la rendition",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Rendition retrieved successfully.",
                                          "data": {
                                            "id": 15,
                                            "outputUrl": "https://res.cloudinary.com/elevideo/video/upload/processed/abc.mp4",
                                            "thumbnailUrl": "https://res.cloudinary.com/elevideo/image/upload/thumb/abc.jpg",
                                            "previewUrl": "https://res.cloudinary.com/elevideo/video/upload/preview/abc.mp4",
                                            "processingMode": "SHORT_AUTO",
                                            "platform": "INSTAGRAM",
                                            "quality": "HIGH"
                                            "backgroundMode": "BLURRED",
                                            "createdAt": "2026-03-08T11:00:00"
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
                                          "path": "/api/v1/projects/1/videos/42/renditions/15"
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "❌ Rendition no encontrada",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "No encontrada",
                                summary = "La rendition no existe o no pertenece al video indicado",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 404,
                                          "error": "RENDITION_NOT_FOUND",
                                          "message": "Rendition no encontrada con id: 15",
                                          "details": [
                                            "Verifica que el ID de la rendition sea correcto y pertenezca al video indicado"
                                          ],
                                          "path": "/api/v1/projects/1/videos/42/renditions/15"
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
                                          "path": "/api/v1/projects/1/videos/42/renditions/15"
                                        }
                                        """
                        )
                )
        )
})
public @interface GetRenditionEndpointDoc {}
