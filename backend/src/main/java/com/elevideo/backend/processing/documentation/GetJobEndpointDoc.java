package com.elevideo.backend.processing.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para GET /api/v1/projects/{projectId}/videos/{videoId}/jobs/{jobId}.
 * Consulta el estado actualizado de un job de procesamiento.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Obtener estado de un job",
        description = """
            Consulta el estado actualizado de un job de procesamiento.
            
            **Flujo de consulta:**
            1. El usuario envía la solicitud para consultar un job específico.
            2. El sistema valida que el job exista y pertenezca al usuario autenticado.
            3. Se consulta el estado actual del job en el microservicio de procesamiento Python.
            4. Los campos progress, status y phase se actualizan localmente.
            5. Si el job está **COMPLETED**, el campo output contiene las URLs generadas.
            6. Si el job está **FAILED**, el campo errorDetail contiene la causa del error.
            
            **Nota:**
            - Este endpoint está diseñado para implementar **polling de estado**
              después de iniciar un job de procesamiento.
            
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Estado del job obtenido exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = {
                                @ExampleObject(
                                        name = "Job en proceso",
                                        summary = "El job está siendo procesado actualmente",
                                        value = """
                                                {
                                                  "success": true,
                                                  "message": "Job retrieved successfully.",
                                                  "data": {
                                                    "jobId": "a3f8c2d1-9e47-4b6a-bc12-7d3e5f90a1b2",
                                                    "status": "PROCESSING",
                                                    "progress": 45,
                                                    "phase": "encoding",
                                                    "processingMode": "SHORT_AUTO",
                                                    "platform": "INSTAGRAM",
                                                    "backgroundMode": "BLUR",
                                                    "quality": "HIGH"
                                                    "output": null,
                                                    "errorDetail": null,
                                                    "createdAt": "2026-03-08T10:00:00"
                                                  }
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "Job completado",
                                        summary = "El job finalizó correctamente — output disponible",
                                        value = """
                                                {
                                                  "success": true,
                                                  "message": "Job retrieved successfully.",
                                                  "data": {
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
                                                    "createdAt": "2026-03-08T10:00:00"
                                                  }
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "Job fallido",
                                        summary = "El job finalizó con error — errorDetail disponible",
                                        value = """
                                                {
                                                  "success": true,
                                                  "message": "Job retrieved successfully.",
                                                  "data": {
                                                    "jobId": "a3f8c2d1-9e47-4b6a-bc12-7d3e5f90a1b2",
                                                    "status": "FAILED",
                                                    "progress": 30,
                                                    "phase": "background_removal",
                                                    "processingMode": "SHORT_AUTO",
                                                    "platform": "INSTAGRAM",
                                                    "backgroundMode": "REMOVE",
                                                    "output": null,
                                                    "errorDetail": "No se detectó sujeto principal en el video para aplicar remoción de fondo",
                                                    "createdAt": "2026-03-08T10:00:00"
                                                  }
                                                }
                                                """
                                )
                        }
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
                                          "path": "/api/v1/projects/1/videos/42/jobs/a3f8c2d1"
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
                                          "path": "/api/v1/projects/1/videos/42/jobs/a3f8c2d1"
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
                                          "path": "/api/v1/projects/1/videos/42/jobs/a3f8c2d1"
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
                                          "path": "/api/v1/projects/1/videos/42/jobs/a3f8c2d1"
                                        }
                                        """
                        )
                )
        )
})
public @interface GetJobEndpointDoc {}
