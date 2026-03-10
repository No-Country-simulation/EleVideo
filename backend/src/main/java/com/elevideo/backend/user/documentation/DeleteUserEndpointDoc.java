package com.elevideo.backend.user.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para DELETE /api/v1/users/me.
 * Elimina permanentemente la cuenta del usuario autenticado.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Eliminar cuenta",
        description = """
            Elimina permanentemente la cuenta del usuario autenticado.
    
            **Flujo de eliminación:**
            1. El usuario envía la solicitud autenticada para eliminar su cuenta.
            2. El sistema valida la identidad del usuario mediante el token JWT.
            3. Se eliminan todos los recursos asociados al usuario.
            4. La cuenta queda eliminada de forma permanente del sistema.
    
            **Advertencia:**
            - Esta operación es irreversible.
            - Se eliminarán todos los datos asociados al usuario.
    
            **Datos eliminados:**
            - **proyectos** — proyectos creados por el usuario
            - **videos** — archivos de video subidos
            - **renditions** — versiones procesadas de los videos
            - **historial de procesamiento** — registros de jobs ejecutados
    
            **Requiere:** Authorization: Bearer {token}
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "✅ Cuenta eliminada exitosamente (sin cuerpo)"
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
                                          "path": "/api/v1/users/me"
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
                                          "path": "/api/v1/users/me"
                                        }
                                        """
                        )
                )
        )
})
public @interface DeleteUserEndpointDoc {}
