package com.elevideo.backend.auth.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Documentación Swagger para POST /api/v1/auth/register.
 * Registra un nuevo usuario en el sistema.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Registrar nuevo usuario",
        description = """
            Crea una nueva cuenta de usuario en el sistema.
    
            **Flujo de registro:**
            1. El usuario envía sus datos de registro.
            2. El sistema valida que el email no esté registrado previamente.
            3. Si los datos son válidos, se crea el usuario con la contraseña encriptada.
            4. Se envía un email de verificación al correo proporcionado.
            5. La API devuelve información básica del usuario registrado.
    
            **Nota:**
            - El usuario debe verificar su dirección de email antes de poder acceder a los recursos protegidos.
            """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "201",
                description = "✅ Usuario registrado exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Registro exitoso",
                                summary = "Respuesta estándar de registro exitoso",
                                value = """
                                        {
                                          "success": true,
                                          "message": "Registration successful. Please check your email to verify your account.",
                                          "data": {
                                            "email": "juan.perez@example.com"
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
                                summary = "Cuando los datos enviados no cumplen las validaciones",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 400,
                                          "error": "VALIDATION_ERROR",
                                          "message": "Error de validación en los campos",
                                          "fieldErrors": {
                                            "email": "debe ser una dirección de correo válida",
                                            "password": "debe contener mayúscula, minúscula, número y carácter especial",
                                            "firstName": "no debe estar vacío"
                                          },
                                          "path": "/api/v1/auth/register"
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "409",
                description = "❌ Email ya registrado",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Usuario ya existe",
                                summary = "El email ya está en uso por otra cuenta",
                                value = """
                                        {
                                          "timestamp": "2026-03-08T10:00:00",
                                          "status": 409,
                                          "error": "USER_ALREADY_EXISTS",
                                          "message": "Ya existe un usuario con ese email",
                                          "details": [
                                            "El email ya está registrado en el sistema"
                                          ],
                                          "path": "/api/v1/auth/register"
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
                                          "path": "/api/v1/auth/register"
                                        }
                                        """
                        )
                )
        )
})
public @interface RegisterEndpointDoc {}
