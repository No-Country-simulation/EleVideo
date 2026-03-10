package com.elevideo.backend.project.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para POST /api/v1/projects.
 * Crea un nuevo proyecto para el usuario autenticado.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Crear proyecto",
        description = """
            Crea un nuevo proyecto asociado al usuario autenticado.
    
            **Flujo de creación:**
            1. El usuario envía los datos del proyecto.
            2. El sistema valida la solicitud y asocia el proyecto al usuario autenticado.
            3. Si los datos son válidos, el proyecto se registra en el sistema.   
    
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "201",
                description = "✅ Proyecto creado exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Proyecto creado",
                                summary = "Respuesta estándar de creación exitosa",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Project created successfully.",
                                          "data": {
                                            "id": 1,
                                            "name": "Campaña de verano",
                                            "description": "Videos para la campaña Q3",
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
                description = "❌ Error de validación",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Campos inválidos",
                                summary = "El nombre del proyecto es obligatorio",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 400,
                                          "error": "VALIDATION_ERROR",
                                          "message": "Error de validación en los campos",
                                          "fieldErrors": {
                                            "name": "no debe estar vacío"
                                          },
                                          "path": "/api/v1/projects"
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
                                          "path": "/api/v1/projects"
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
                                          "path": "/api/v1/projects"
                                        }
                                        """
                        )
                )
        )
})
public @interface CreateProjectEndpointDoc {}
