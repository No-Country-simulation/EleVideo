package com.elevideo.backend.processing.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para POST /api/v1/projects/{projectId}/videos/{videoId}/jobs/{jobId}/cancel.
 * Solicita la cancelación de un job de procesamiento activo.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Cancelar job",
        description = """
            Solicita la cancelación de un job de procesamiento activo.
            
            **Flujo de cancelación:**
            1. El usuario envía la solicitud de cancelación para un job específico.
            2. El sistema valida que el job exista y pertenezca al usuario autenticado.
            3. Se envía una solicitud de cancelación al microservicio de procesamiento.
            4. El estado del job se actualiza cuando el sistema de procesamiento confirma la cancelación.
            
            **Nota:**
            - La cancelación es una solicitud, no una garantía inmediata.
            - El estado final del job puede consultarse mediante GET /api/v1/jobs/{jobId}.
            
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Cancelación solicitada exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Cancelación solicitada",
                                summary = "La solicitud de cancelación fue aceptada por el servicio de procesamiento",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Job cancellation requested successfully.",
                                          "data": {
                                            "job_id": "a3f8c2d1-9e47-4b6a-bc12-7d3e5f90a1b2",
                                            "message": "Cancelación enviada al servicio de procesamiento",
                                            "previous_status": "PROCESSING"
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
                                          "path": "/api/v1/projects/1/videos/42/jobs/a3f8c2d1/cancel"
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "❌ Job o video no encontrado",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "No encontrado",
                                summary = "El job no existe o no pertenece al video indicado",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 404,
                                          "error": "JOB_NOT_FOUND",
                                          "message": "Job no encontrado: a3f8c2d1-9e47-4b6a-bc12-7d3e5f90a1b2",
                                          "details": [
                                            "Verifica que el ID del job sea correcto y pertenezca al video indicado"
                                          ],
                                          "path": "/api/v1/projects/1/videos/42/jobs/a3f8c2d1/cancel"
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "503",
                description = "❌ Servicio de procesamiento no disponible",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Servicio no disponible",
                                summary = "El microservicio Python no respondió correctamente",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 503,
                                          "error": "PROCESSING_SERVICE_UNAVAILABLE",
                                          "message": "El servicio de procesamiento no está disponible en este momento",
                                          "details": [
                                            "Intenta de nuevo en unos momentos"
                                          ],
                                          "path": "/api/v1/projects/1/videos/42/jobs/a3f8c2d1/cancel"
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
                                          "path": "/api/v1/projects/1/videos/42/jobs/a3f8c2d1/cancel"
                                        }
                                        """
                        )
                )
        )
})
public @interface CancelJobEndpointDoc {}
