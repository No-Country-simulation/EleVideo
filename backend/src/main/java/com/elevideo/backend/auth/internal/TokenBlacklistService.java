package com.elevideo.backend.auth.internal;

import com.elevideo.backend.shared.security.TokenBlacklistPort;

import java.time.LocalDateTime;

/**
 * Servicio de blacklist de tokens JWT.
 * Extiende TokenBlacklistPort para que JwtService pueda inyectarlo
 * sin conocer la implementación concreta de auth.
 */
public interface TokenBlacklistService extends TokenBlacklistPort {

    void addToBlacklist(String token, LocalDateTime expiresAt);
}
