package com.elevideo.backend.shared.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Provee el UUID del usuario autenticado en el contexto de seguridad actual.
 * Usado por los servicios que necesitan saber quién hace la petición.
 */
@Component
public class CurrentUserProvider {

    public UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No hay un usuario autenticado en el contexto actual.");
        }

        CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();
        return principal.getId();
    }
}
