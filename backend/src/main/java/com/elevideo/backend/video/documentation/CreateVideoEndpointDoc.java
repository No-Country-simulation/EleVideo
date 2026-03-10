package com.elevideo.backend.video.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para POST /api/v1/projects/{projectId}/videos.
 * Sube un archivo de video al proyecto y lo almacena en Cloudinary.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Subir video",
        description = """
            Sube un archivo de video a un proyecto y lo almacena en Cloudinary.
    
            **Request:** multipart/form-data
            - title — título del video (requerido, no vacío)
            - video — archivo de video (requerido, formatos: mp4, mov, tamaño máximo: 50 MB)
    
            **Flujo de subida:**
            1. El usuario envía el archivo de video y su título.
            2. El sistema valida que el proyecto pertenezca al usuario autenticado.
            3. El archivo se sube al servicio de almacenamiento.
            4. Se extraen automáticamente los metadatos del video (duración, resolución y tamaño).
            5. Se crea el registro del video en el sistema con estado **UPLOADED**.
            6. Si la persistencia falla, el archivo previamente subido se elimina del almacenamiento.
    
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "201",
                description = "✅ Video subido exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Video creado",
                                summary = "Respuesta estándar con los metadatos del video subido",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Video uploaded successfully.",
                                          "data": {
                                            "id": 42,
                                            "title": "Demo del producto",
                                            "videoUrl": "https://res.cloudinary.com/elevideo/video/upload/v1/Elevideo/xyz.mp4",
                                            "format": "mp4",
                                            "durationInMillis": 95000,
                                            "sizeInBytes": 18350080,
                                            "width": 1920,
                                            "height": 1080,
                                            "status": "UPLOADED",
                                            "projectId": 1,
                                            "createdAt": "2026-03-08T10:00:00",
                                            "updatedAt": "2026-03-08T10:00:00"
                                          }
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "❌ Error de validación o formato no soportado",
                content = @Content(
                        mediaType = "application/json",
                        examples = {
                                @ExampleObject(
                                        name = "Título ausente",
                                        summary = "El título del video es obligatorio",
                                        value = """
                                                {
                                                  "timestamp": "2026-03-08T10:00:00",
                                                  "status": 400,
                                                  "error": "VALIDATION_ERROR",
                                                  "message": "Error de validación en los campos",
                                                  "fieldErrors": {
                                                    "title": "no debe estar vacío"
                                                  },
                                                  "path": "/api/v1/projects/1/videos"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "Formato no soportado",
                                        summary = "Solo se admiten archivos mp4 y mov",
                                        value = """
                                                {
                                                  "timestamp": "2026-03-08T10:00:00",
                                                  "status": 400,
                                                  "error": "VALIDATION_ERROR",
                                                  "message": "Error de validación en los campos",
                                                  "fieldErrors": {
                                                    "video": "solo se admiten formatos mp4 y mov"
                                                  },
                                                  "path": "/api/v1/projects/1/videos"
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
                                          "path": "/api/v1/projects/1/videos"
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
                                          "path": "/api/v1/projects/1/videos"
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "502",
                description = "❌ Fallo al subir el archivo a Cloudinary",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Error de almacenamiento",
                                summary = "El proveedor de almacenamiento rechazó o no procesó el archivo",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 502,
                                          "error": "STORAGE_ERROR",
                                          "message": "No se pudo subir el archivo al proveedor de almacenamiento",
                                          "details": [
                                            "Intenta de nuevo en unos momentos"
                                          ],
                                          "path": "/api/v1/projects/1/videos"
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
                                          "path": "/api/v1/projects/1/videos"
                                        }
                                        """
                        )
                )
        )
})
public @interface CreateVideoEndpointDoc {}
