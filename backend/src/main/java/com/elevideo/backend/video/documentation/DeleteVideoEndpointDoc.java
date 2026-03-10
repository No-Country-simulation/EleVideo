package com.elevideo.backend.video.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para DELETE /api/v1/projects/{projectId}/videos/{videoId}.
 * Elimina un video del proyecto y lo borra de Cloudinary.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Eliminar video",
        description = """
            Elimina permanentemente un video del proyecto.
    
            **Flujo de eliminación:**
            1. El usuario envía la solicitud para eliminar un video específico.
            2. El sistema valida que el video pertenezca al usuario autenticado.
            3. Se eliminan los recursos asociados al video.
            4. El archivo original se elimina del almacenamiento en Cloudinary.
            5. Se elimina el registro del video del sistema.
    
            **Advertencia:**
            - Esta operación es irreversible.
            - También se eliminarán los jobs de procesamiento y las renditions asociadas al video.
    
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "✅ Video eliminado exitosamente (sin cuerpo)"
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
public @interface DeleteVideoEndpointDoc {}
