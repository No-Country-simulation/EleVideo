package com.elevideo.backend.auth.api;

import com.elevideo.backend.auth.api.dto.*;

/**
 * API pública del módulo auth.
 * Solo esta interfaz puede ser usada por otros módulos.
 */
public interface AuthService {

    /**
     * Registra un nuevo usuario, envía email de verificación.
     *
     * @throws com.elevideo.backend.auth.internal.UserAlreadyExistsException si el email ya está registrado
     */
    RegisterResponse register(RegisterRequest request);

    /**
     * Autentica al usuario y retorna un JWT de acceso.
     */
    LoginResponse login(LoginRequest request);

    /**
     * Verifica el email del usuario mediante el token recibido por correo.
     */
    VerifyEmailResponse verifyEmail(VerifyEmailRequest request);

    /**
     * Inicia el proceso de recuperación de contraseña.
     * No revela si el email existe o no (seguridad por diseño).
     */
    void forgotPassword(ForgotPasswordRequest request);

    /**
     * Restablece la contraseña usando el token de recuperación.
     */
    void resetPassword(ResetPasswordRequest request);
}
