package com.elevideo.backend.user.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para PATCH /api/v1/users/me/password.
 * Cambia la contraseña del usuario autenticado.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Cambiar contraseña",
        description = """
        Cambia la contraseña del usuario autenticado.

        **Flujo de cambio de contraseña:**
        1. El usuario envía su contraseña actual y la nueva contraseña.
        2. El sistema valida la identidad del usuario mediante el token JWT.
        3. Se verifica que **currentPassword** coincida con la contraseña almacenada.
        4. Se valida que **newPassword** cumpla los requisitos de seguridad y sea diferente de la contraseña actual.
        5. Si las validaciones son correctas, la contraseña se actualiza en el sistema.

        **Validaciones aplicadas:**
        - **currentPassword** debe coincidir con la contraseña almacenada.
        - **newPassword** debe ser diferente de la contraseña actual y debe cumplir los requisitos de seguridad:
          mayúscula, minúscula, número y carácter especial.

        **Requiere:** Authorization: Bearer {token}
        """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "✅ Contraseña actualizada exitosamente (sin cuerpo)"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "❌ Error de validación, contraseña actual incorrecta o contraseña igual a la anterior",
                content = @Content(
                        mediaType = "application/json",
                        examples = {
                                @ExampleObject(
                                        name = "Campos inválidos",
                                        summary = "La nueva contraseña no cumple los requisitos de seguridad",
                                        value = """
                                                {
                                                  "timestamp": "2026-03-08T10:00:00",
                                                  "status": 400,
                                                  "error": "VALIDATION_ERROR",
                                                  "message": "Error de validación en los campos",
                                                  "fieldErrors": {
                                                    "newPassword": "debe contener mayúscula, minúscula, número y carácter especial"
                                                  },
                                                  "path": "/api/v1/users/me/password"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "Contraseña actual incorrecta",
                                        summary = "La contraseña actual enviada no coincide con la almacenada",
                                        value = """
                                                {
                                                  "timestamp": "2026-03-08T10:00:00",
                                                  "status": 400,
                                                  "error": "INVALID_CURRENT_PASSWORD",
                                                  "message": "La contraseña actual es incorrecta",
                                                  "details": [
                                                    "Verifica que hayas ingresado tu contraseña actual correctamente"
                                                  ],
                                                  "path": "/api/v1/users/me/password"
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "Contraseña igual a la anterior",
                                        summary = "La nueva contraseña es idéntica a la actual",
                                        value = """
                                                {
                                                  "timestamp": "2026-03-08T10:00:00",
                                                  "status": 400,
                                                  "error": "SAME_PASSWORD",
                                                  "message": "La nueva contraseña debe ser diferente a la actual",
                                                  "details": [
                                                    "Elige una contraseña que no hayas usado anteriormente"
                                                  ],
                                                  "path": "/api/v1/users/me/password"
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
                                          "path": "/api/v1/users/me/password"
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
                                          "path": "/api/v1/users/me/password"
                                        }
                                        """
                        )
                )
        )
})
public @interface ChangePasswordEndpointDoc {}
