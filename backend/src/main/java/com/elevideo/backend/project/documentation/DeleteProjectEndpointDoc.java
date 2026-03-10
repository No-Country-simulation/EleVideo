package com.elevideo.backend.project.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para DELETE /api/v1/projects/{projectId}.
 * Elimina un proyecto y todos sus recursos asociados.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Eliminar proyecto",
        description = """
            Elimina permanentemente un proyecto y todos sus recursos asociados.
    
            **Flujo de eliminación:**
            1. El usuario envía la solicitud para eliminar un proyecto específico.
            2. El sistema valida que el proyecto pertenezca al usuario autenticado.
            3. Si el proyecto existe, se eliminan todos los recursos asociados:
               videos, jobs de procesamiento y renditions.
            4. El proyecto se elimina definitivamente del sistema.
    
            **Advertencia:**
            - Esta operación es irreversible.
    
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "✅ Proyecto eliminado exitosamente (sin cuerpo)"
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
public @interface DeleteProjectEndpointDoc {}
