package com.elevideo.backend.processing.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para DELETE /api/v1/projects/{projectId}/videos/{videoId}/renditions/{renditionId}.
 * Elimina una rendition del video.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Eliminar rendition",
        description = """
            Elimina permanentemente una rendition de video.
            
            **Flujo de eliminación:**
            1. El usuario envía la solicitud para eliminar una rendition específica.
            2. El sistema valida que la rendition exista y pertenezca al usuario autenticado.
            3. Se elimina el archivo procesado del almacenamiento.
            4. Se elimina el registro de la rendition del sistema.
            
            **Advertencia:**
            - Esta operación es irreversible.
            - El archivo procesado también se eliminará del almacenamiento.
            
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "✅ Rendition eliminada exitosamente (sin cuerpo)"
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
public @interface DeleteRenditionEndpointDoc {}
