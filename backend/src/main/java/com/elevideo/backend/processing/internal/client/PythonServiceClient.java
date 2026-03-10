package com.elevideo.backend.processing.internal.client;

import com.elevideo.backend.shared.security.JwtService;
import com.elevideo.backend.shared.security.TokenPurpose;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.UUID;

/**
 * Cliente HTTP hacia el microservicio Python de procesamiento.
 * Gestiona autenticación (JWT + API Key) y manejo de errores centralizado.
 */
@Slf4j
@Component
public class PythonServiceClient {

    private final JwtService jwtService;
    private final RestClient restClient;

    @Value("${python.service.url}")
    private String pythonServiceUrl;

    @Value("${python.service.api-key}")
    private String serviceApiKey;

    public PythonServiceClient(JwtService jwtService) {
        this.jwtService  = jwtService;
        this.restClient  = RestClient.builder()
                .requestFactory(new SimpleClientHttpRequestFactory())
                .build();
    }

    public <T> T post(String path, Object body, Class<T> responseType, UUID userId) {
        log.debug("POST Python service | path={} | userId={}", path, userId);
        try {
            return restClient.post()
                    .uri(pythonServiceUrl + path)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Service-Key", serviceApiKey)
                    .header("Authorization", "Bearer " + serviceToken(userId))
                    .body(body)
                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException e)  { return handleClient(e, path); }
        catch (HttpServerErrorException e)    { return handleServer(e, path); }
        catch (ResourceAccessException e)     { throw unavailable(path, e);   }
    }

    public <T> T get(String path, Class<T> responseType, UUID userId) {
        log.debug("GET Python service | path={} | userId={}", path, userId);
        try {
            return restClient.get()
                    .uri(pythonServiceUrl + path)
                    .header("X-Service-Key", serviceApiKey)
                    .header("Authorization", "Bearer " + serviceToken(userId))
                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException e)  { return handleClient(e, path); }
        catch (HttpServerErrorException e)    { return handleServer(e, path); }
        catch (ResourceAccessException e)     { throw unavailable(path, e);   }
    }

    public <T> T postEmpty(String path, Class<T> responseType, UUID userId) {
        log.debug("POST (sin body) Python service | path={} | userId={}", path, userId);
        try {
            return restClient.post()
                    .uri(pythonServiceUrl + path)
                    .header("X-Service-Key", serviceApiKey)
                    .header("Authorization", "Bearer " + serviceToken(userId))
                    .retrieve()
                    .body(responseType);
        } catch (HttpClientErrorException e)  { return handleClient(e, path); }
        catch (HttpServerErrorException e)    { return handleServer(e, path); }
        catch (ResourceAccessException e)     { throw unavailable(path, e);   }
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    private String serviceToken(UUID userId) {
        return jwtService.generateServiceToken(userId);
    }

    private <T> T handleClient(HttpClientErrorException e, String path) {
        log.warn("Error cliente Python | path={} | status={}", path, e.getStatusCode());
        if (e.getStatusCode() == HttpStatus.NOT_FOUND)
            throw new PythonServiceException("El recurso solicitado no existe.");
        if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.FORBIDDEN)
            throw new PythonServiceException("Error de configuración interna. Contacta al administrador.");
        if (e.getStatusCode() == HttpStatus.BAD_REQUEST)
            throw new PythonServiceException("Solicitud inválida: " + e.getResponseBodyAsString());
        throw new PythonServiceException("Error al comunicarse con el servicio de procesamiento.");
    }

    private <T> T handleServer(HttpServerErrorException e, String path) {
        log.error("Error servidor Python | path={} | status={}", path, e.getStatusCode());
        throw new PythonServiceException("El servicio de procesamiento falló. Intenta de nuevo.");
    }

    private PythonServiceException unavailable(String path, ResourceAccessException e) {
        log.error("No se pudo conectar al microservicio Python | path={} | error={}", path, e.getMessage());
        return new PythonServiceException("El servicio de procesamiento no está disponible. Intenta más tarde.");
    }
}
