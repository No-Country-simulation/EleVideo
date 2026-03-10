package com.elevideo.backend.shared.security;

/**
 * Puerto que permite a JwtService verificar si un token está en la blacklist
 * sin acoplarse al módulo auth ni a ninguna implementación concreta.
 *
 * La implementación concreta vive en auth/internal.
 */
public interface TokenBlacklistPort {
    boolean isBlacklisted(String token);
}