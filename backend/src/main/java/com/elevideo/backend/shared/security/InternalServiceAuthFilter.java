package com.elevideo.backend.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro dedicado para endpoints internos (/api/internal/**).
 * Valida el header X-Service-Key y otorga el rol ROLE_INTERNAL_SERVICE
 * al request, permitiendo que Spring Security proteja estos endpoints
 * de forma estructural en lugar de validaciones manuales en cada controller.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InternalServiceAuthFilter extends OncePerRequestFilter {

    private static final String SERVICE_KEY_HEADER = "X-Service-Key";
    private static final String INTERNAL_PATH      = "/api/internal/";

    @Value("${python.service.api-key}")
    private String validServiceApiKey;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith(INTERNAL_PATH);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String providedKey = request.getHeader(SERVICE_KEY_HEADER);

        if (providedKey != null && providedKey.equals(validServiceApiKey)) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    "internal-service",
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_INTERNAL_SERVICE"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.debug("Internal service authenticated for [{}]", request.getRequestURI());
        } else {
            log.warn("Unauthorized internal service request to [{}] — missing or invalid X-Service-Key",
                    request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }
}
