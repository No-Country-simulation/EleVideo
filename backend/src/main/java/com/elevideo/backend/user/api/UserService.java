package com.elevideo.backend.user.api;

import com.elevideo.backend.user.api.dto.AuthenticatedUserResponse;
import com.elevideo.backend.user.api.dto.ChangePasswordRequest;
import com.elevideo.backend.user.api.dto.UserRes;
import com.elevideo.backend.user.api.dto.UserUpdateRequest;

import java.util.UUID;

/**
 * API pública del módulo user.
 * Solo esta interfaz puede ser usada por otros módulos.
 */
public interface UserService {

    /**
     * Retorna los datos del usuario autenticado en el contexto actual.
     */
    AuthenticatedUserResponse getAuthenticatedUser();

    /**
     * Actualiza el nombre y apellido del usuario autenticado.
     */
    AuthenticatedUserResponse updateUser(UserUpdateRequest request);

    /**
     * Cambia la contraseña del usuario autenticado.
     */
    void changePassword(ChangePasswordRequest request);

    /**
     * Elimina la cuenta del usuario autenticado.
     */
    void deleteUser();

    /**
     * Marca el email del usuario como verificado y publica UserEmailVerifiedEvent.
     * Llamado exclusivamente por el módulo auth.
     */
    void verifyEmail(UUID userId);
}
