package com.elevideo.backend.auth.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para POST /api/v1/auth/login.
 * Autentica un usuario y devuelve un token JWT.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Iniciar sesión",
        description = """
            Autentica al usuario mediante email y contraseña.
    
            **Flujo de autenticación:**
            1. El usuario envía sus credenciales (email y contraseña).
            2. El sistema valida que las credenciales sean correctas.
            3. Si la autenticación es exitosa, se genera un token JWT.
            4. El token se devuelve junto con información básica del usuario.
    
            **Uso del token:**
            - El token debe enviarse en las peticiones protegidas mediante el header:
              Authorization: Bearer {token}
    
            **Nota:**
            - El token tiene una duración limitada configurada en el servidor.
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "✅ Autenticación exitosa",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Login exitoso",
                                summary = "Respuesta estándar de autenticación exitosa",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Login successful.",
                                          "data": {
                                            "token": "eyJhbGciOiJIUzI1NiJ9...",
                                            "user": {
                                              "id": "6fb1f53e-f1c6-4c87-9337-96062903b8f8",
                                              "firstName": "Juan José",
                                              "lastName": "Pérez Gómez",
                                              "email": "juan.perez@example.com",
                                              "emailVerified": true
                                            }
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
                                summary = "Cuando faltan campos requeridos o tienen formato incorrecto",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 400,
                                          "error": "VALIDATION_ERROR",
                                          "message": "Error de validación en los campos",
                                          "fieldErrors": {
                                            "email": "debe ser una dirección de correo válida",
                                            "password": "no debe estar vacío"
                                          },
                                          "path": "/api/v1/auth/login"
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "❌ Credenciales incorrectas",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Credenciales inválidas",
                                summary = "Email o contraseña incorrectos",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 401,
                                          "error": "BAD_CREDENTIALS",
                                          "message": "Credenciales inválidas",
                                          "details": [
                                            "El correo electrónico o la contraseña son incorrectos"
                                          ],
                                          "path": "/api/v1/auth/login"
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
                                          "path": "/api/v1/auth/login"
                                        }
                                        """
                        )
                )
        )
})
public @interface LoginEndpointDoc {}
