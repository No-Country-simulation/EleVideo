package com.elevideo.backend.shared.security;

import com.elevideo.backend.shared.config.JwtExpirationProperties;
import com.elevideo.backend.shared.exception.base.DomainException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtExpirationProperties jwtProperties;
    private final TokenBlacklistPort blacklistPort;
    private SecretKey key;

    @PostConstruct
    public void init() {
        if (jwtProperties.getSecret().length() < 32) {
            throw new IllegalStateException("La clave secreta JWT debe tener al menos 32 caracteres.");
        }
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // ----------------------------------------------------------------
    // Generación de tokens
    // ----------------------------------------------------------------

    public String generateUserToken(JwtData jwtData, TokenPurpose purpose) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", jwtData.email());
        claims.put("scope", purpose.name());

        if (purpose == TokenPurpose.AUTHENTICATION) {
            claims.put("firstName", jwtData.firstName());
            claims.put("lastName",  jwtData.lastName());
        }

        return buildToken(
                jwtData.id().toString(),
                jwtProperties.getIssuer(),
                purpose.resolveAudience(),
                claims,
                purpose.resolveExpiration(jwtProperties)
        );
    }

    public String generateServiceToken(UUID userId) {
        return buildToken(
                userId.toString(),
                jwtProperties.getIssuer(),
                "python-service",
                Map.of(
                        "scope",      TokenPurpose.PYTHON_SERVICE.name(),
                        "token_type", "DELEGATED_SERVICE"
                ),
                TokenPurpose.PYTHON_SERVICE.resolveExpiration(jwtProperties)
        );
    }

    // ----------------------------------------------------------------
    // Validación de tokens
    // ----------------------------------------------------------------

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return email.equals(userDetails.getUsername())
                && !isTokenExpired(token)
                && !blacklistPort.isBlacklisted(token);
    }

    public void validateEmailVerificationToken(String token) {
        validateTokenNotExpiredOrBlacklisted(token);
        requirePurpose(token, TokenPurpose.EMAIL_VERIFICATION,
                "El token no es válido para la verificación de correo electrónico.");
    }

    public void validateResetPasswordToken(String token) {
        validateTokenNotExpiredOrBlacklisted(token);
        requirePurpose(token, TokenPurpose.PASSWORD_RESET,
                "El token no es válido para la recuperación de contraseña.");
    }

    // ----------------------------------------------------------------
    // Extracción de claims
    // ----------------------------------------------------------------

    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    public TokenPurpose extractPurpose(String token) {
        return TokenPurpose.valueOf(
                extractClaim(token, claims -> claims.get("scope", String.class))
        );
    }

    public LocalDateTime extractExpiration(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return LocalDateTime.ofInstant(expiration.toInstant(), ZoneOffset.UTC);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(LocalDateTime.now(ZoneOffset.UTC));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    // ----------------------------------------------------------------
    // Helpers privados
    // ----------------------------------------------------------------

    private String buildToken(String subject, String issuer, String audience,
                               Map<String, Object> claims, long expirationMillis) {
        Instant now        = Instant.now();
        Instant expiration = now.plusMillis(expirationMillis);

        return Jwts.builder()
                .subject(subject)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .claim("aud", audience)
                .claims(claims)
                .signWith(key)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private void validateTokenNotExpiredOrBlacklisted(String token) {
        if (isTokenExpired(token)) {
            throw new TokenExpiredException("El token ha expirado. Solicita uno nuevo.");
        }
        if (blacklistPort.isBlacklisted(token)) {
            throw new TokenInvalidException("Este token ya fue utilizado y no puede reutilizarse.");
        }
    }

    private void requirePurpose(String token, TokenPurpose expected, String errorMessage) {
        if (extractPurpose(token) != expected) {
            throw new TokenInvalidException(errorMessage);
        }
    }

    // ----------------------------------------------------------------
    // Excepciones específicas de JWT (no exponen infraestructura)
    // ----------------------------------------------------------------

    public static final class TokenExpiredException extends DomainException {
        public TokenExpiredException(String message) { super(message); }
    }

    public static final class TokenInvalidException extends DomainException {
        public TokenInvalidException(String message) { super(message); }
    }

    /**
     * DTO interno para datos necesarios al generar un token.
     */
    public record JwtData(UUID id, String email, String firstName, String lastName) {}
}