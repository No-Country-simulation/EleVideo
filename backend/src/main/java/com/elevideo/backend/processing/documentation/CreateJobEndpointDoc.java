package com.elevideo.backend.processing.documentation;

import com.elevideo.backend.processing.api.dto.VideoProcessRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para POST /api/v1/projects/{projectId}/videos/{videoId}/jobs.
 * Inicia un nuevo job de procesamiento para el video indicado.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Iniciar job de procesamiento",
        description = """
            Inicia el procesamiento de un video y crea un job asíncrono.
            
            **Flujo de procesamiento:**
            1. El usuario envía una solicitud de procesamiento para un video existente.
            2. El sistema valida que el video pertenezca al usuario autenticado.
            3. Se crea un registro de job en el sistema con los parámetros enviados.
            4. La solicitud se envía al microservicio de procesamiento Python.
            5. La API devuelve un statusUrl que permite consultar el estado del procesamiento.
            
            **Nota:**
            - El procesamiento es asíncrono, por lo que la API devuelve 202 Accepted.
            - El estado del job puede consultarse mediante GET /api/v1/jobs/{jobId}.
            
            ---
            
            **Modos de procesamiento (processingMode):**
            
            - **vertical** — convierte el video completo a formato vertical 9:16 con recorte inteligente.
            - **short_auto** — extrae automáticamente el mejor segmento del video.  
              Requiere shortAutoDuration entre **5 y 60 segundos**.
            - **short_manual** — extrae un segmento específico definido por el usuario.  
              Requiere shortOptions.startTime y shortOptions.duration.
            
            **Campos requeridos:**
            - processingMode
            - platform
            - quality
            - backgroundMode
            
            **Campos condicionales:**
            - shortAutoDuration — requerido si processingMode = short_auto
            - shortOptions — requerido si processingMode = short_manual
            - advancedOptions — opcional en todos los modos
            
            ---
            
            **Valores válidos:**
            
            | Campo | Valores |
            |---|---|
            | processingMode | vertical, short_auto, short_manual |
            | platform | tiktok, instagram, youtube_shorts |
            | quality | fast, normal, high |
            | backgroundMode | smart_crop, blurred, black |
            
            **Respuesta:**
            - **202 Accepted** — job de procesamiento iniciado correctamente.
            
            **Requiere:** Authorization: Bearer {token}
            """
)
@RequestBody(
        description = "Configuración del procesamiento según el modo seleccionado",
        required = true,
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VideoProcessRequest.class),
                examples = {
                        @ExampleObject(
                                name = "vertical",
                                summary = "Convertir video completo a 9:16",
                                description = """
                                        Convierte el video completo a formato vertical con recorte inteligente.
                                        `shortOptions` y `shortAutoDuration` no deben enviarse.
                                        `advancedOptions` es opcional para ajustar el algoritmo de recorte.
                                        """,
                                value = """
                                        {
                                          "processingMode": "vertical",
                                          "platform": "tiktok",
                                          "quality": "normal",
                                          "backgroundMode": "smart_crop",
                                          "advancedOptions": {
                                            "headroomRatio": 0.2,
                                            "smoothingStrength": 0.9
                                          }
                                        }
                                        """
                        ),
                        @ExampleObject(
                                name = "short_auto",
                                summary = "Generar short automático de 30 segundos",
                                description = """
                                        El servicio detecta automáticamente el mejor segmento del video.
                                        `shortAutoDuration` es obligatorio (entre 5 y 60 segundos).
                                        `shortOptions` no debe enviarse.
                                        """,
                                value = """
                                        {
                                          "processingMode": "short_auto",
                                          "platform": "instagram",
                                          "quality": "high",
                                          "backgroundMode": "blurred",
                                          "shortAutoDuration": 30
                                        }
                                        """
                        ),
                        @ExampleObject(
                                name = "short_manual",
                                summary = "Generar short desde el segundo 120 por 15 segundos",
                                description = """
                                        Extrae el segmento indicado por el usuario y lo convierte a vertical.
                                        `shortOptions.startTime` es el segundo de inicio (≥ 0).
                                        `shortOptions.duration` es la duración en segundos (entre 5 y 60).
                                        `shortAutoDuration` no debe enviarse.
                                        """,
                                value = """
                                        {
                                          "processingMode": "short_manual",
                                          "platform": "youtube_shorts",
                                          "quality": "normal",
                                          "backgroundMode": "black",
                                          "shortOptions": {
                                            "startTime": 120.0,
                                            "duration": 15
                                          }
                                        }
                                        """
                        ),
                        @ExampleObject(
                                name = "short_manual con advancedOptions",
                                summary = "Short manual con opciones avanzadas de composición",
                                description = """
                                        Mismo modo manual pero ajustando el comportamiento del algoritmo
                                        de encuadre mediante `advancedOptions`.
                                        """,
                                value = """
                                        {
                                          "processingMode": "short_manual",
                                          "platform": "tiktok",
                                          "quality": "high",
                                          "backgroundMode": "blurred",
                                          "shortOptions": {
                                            "startTime": 45.0,
                                            "duration": 20
                                          },
                                          "advancedOptions": {
                                            "headroomRatio": 0.15,
                                            "smoothingStrength": 0.85,
                                            "applySharpening": true,
                                            "useRuleOfThirds": true,
                                            "edgePadding": 10
                                          }
                                        }
                                        """
                        )
                }
        )
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "202",
                description = "✅ Job de procesamiento iniciado",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Job iniciado",
                                summary = "Respuesta con el ID del job y la URL de seguimiento",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Processing job created successfully.",
                                          "data": {
                                            "job_id": "a3f8c2d1-9e47-4b6a-bc12-7d3e5f90a1b2",
                                            "status": "pending",
                                            "processing_mode": "short_auto",
                                            "status_url": "/api/v1/projects/1/videos/42/jobs/a3f8c2d1-9e47-4b6a-bc12-7d3e5f90a1b2"
                                          }
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "❌ Error de validación",
                content = @Content(
                        mediaType = "application/json",
                        examples = {
                                @ExampleObject(
                                        name = "Campos requeridos ausentes",
                                        summary = "Faltan campos obligatorios en la solicitud",
                                        value = """
                                                {
                                                  "timestamp": "2026-03-08T10:00:00",
                                                  "status": 400,
                                                  "error": "VALIDATION_ERROR",
                                                  "message": "Error de validación en los campos",
                                                  "fieldErrors": {
                                                    "processingMode": "no debe ser nulo",
                                                    "platform": "no debe ser nulo",
                                                    "quality": "no debe ser nulo",
                                                    "backgroundMode": "no debe ser nulo"
                                                  },
                                                  "path": "/api/v1/projects/1/videos/42/jobs"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "short_auto sin shortAutoDuration",
                                        summary = "Se envió short_auto pero falta el campo shortAutoDuration",
                                        value = """
                                                {
                                                  "timestamp": "2026-03-08T10:00:00",
                                                  "status": 400,
                                                  "error": "VALIDATION_ERROR",
                                                  "message": "Error de validación en los campos",
                                                  "fieldErrors": {
                                                    "shortAutoDuration": "requerido cuando processingMode = short_auto"
                                                  },
                                                  "path": "/api/v1/projects/1/videos/42/jobs"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "short_manual sin shortOptions",
                                        summary = "Se envió short_manual pero falta el campo shortOptions",
                                        value = """
                                                {
                                                  "timestamp": "2026-03-08T10:00:00",
                                                  "status": 400,
                                                  "error": "VALIDATION_ERROR",
                                                  "message": "Error de validación en los campos",
                                                  "fieldErrors": {
                                                    "shortOptions": "requerido cuando processingMode = short_manual"
                                                  },
                                                  "path": "/api/v1/projects/1/videos/42/jobs"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "Duración fuera de rango",
                                        summary = "shortAutoDuration o shortOptions.duration fuera del rango permitido (5–60 segundos)",
                                        value = """
                                                {
                                                  "timestamp": "2026-03-08T10:00:00",
                                                  "status": 400,
                                                  "error": "VALIDATION_ERROR",
                                                  "message": "Error de validación en los campos",
                                                  "fieldErrors": {
                                                    "shortAutoDuration": "debe estar entre 5 y 60"
                                                  },
                                                  "path": "/api/v1/projects/1/videos/42/jobs"
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
public @interface CreateJobEndpointDoc {}