package com.elevideo.backend.shared.exception;

import com.elevideo.backend.shared.exception.base.ConflictException;
import com.elevideo.backend.shared.exception.base.ForbiddenException;
import com.elevideo.backend.shared.exception.base.NotFoundException;
import com.elevideo.backend.shared.web.ApiResult;
import com.elevideo.backend.user.internal.InvalidCurrentPasswordException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ----------------------------------------------------------------
    // Excepciones de dominio
    // ----------------------------------------------------------------

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), request);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex, HttpServletRequest request) {
        return buildError(HttpStatus.FORBIDDEN, "FORBIDDEN", ex.getMessage(), request);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex, HttpServletRequest request) {
        return buildError(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidCurrentPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCurrentPassword(
            InvalidCurrentPasswordException ex,
            HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "INVALID_CURRENT_PASSWORD", ex.getMessage(), request);
    }

    // ----------------------------------------------------------------
    // Validación de request
    // ----------------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        f -> f.getDefaultMessage() != null ? f.getDefaultMessage() : "Valor inválido",
                        (first, second) -> first
                ));

        ValidationErrorResponse body = ValidationErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("VALIDATION_ERROR")
                .message("Error de validación en los campos")
                .fieldErrors(fieldErrors)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    // ----------------------------------------------------------------
    // Seguridad
    // ----------------------------------------------------------------

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "BAD_CREDENTIALS", "Invalid credentials", request);
    }

    // ----------------------------------------------------------------
    // Servicios externos
    // ----------------------------------------------------------------

    @ExceptionHandler(com.elevideo.backend.processing.internal.client.PythonServiceException.class)
    public ResponseEntity<ErrorResponse> handlePythonService(
            com.elevideo.backend.processing.internal.client.PythonServiceException ex,
            HttpServletRequest request) {
        log.warn("Python service error [{}]: {}", request.getRequestURI(), ex.getMessage());
        return buildError(HttpStatus.SERVICE_UNAVAILABLE, "PROCESSING_SERVICE_ERROR", ex.getMessage(), request);
    }

    // ----------------------------------------------------------------
    // JWT
    // ----------------------------------------------------------------

    @ExceptionHandler(com.elevideo.backend.shared.security.JwtService.TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpired(
            com.elevideo.backend.shared.security.JwtService.TokenExpiredException ex,
            HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED", ex.getMessage(), request);
    }

    @ExceptionHandler(com.elevideo.backend.shared.security.JwtService.TokenInvalidException.class)
    public ResponseEntity<ErrorResponse> handleTokenInvalid(
            com.elevideo.backend.shared.security.JwtService.TokenInvalidException ex,
            HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "TOKEN_INVALID", ex.getMessage(), request);
    }

    // ----------------------------------------------------------------
    // Fallback
    // ----------------------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Error no controlado en [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR",
                "Error interno del servidor", request);
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    private ResponseEntity<ErrorResponse> buildError(
            HttpStatus status, String error, String message, HttpServletRequest request) {

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .details(List.of())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(body);
    }

    // ----------------------------------------------------------------
    // Response bodies
    // ----------------------------------------------------------------

    @Builder
    public record ErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            String message,
            List<String> details,
            String path
    ) {}

    @Builder
    public record ValidationErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            String message,
            Map<String, String> fieldErrors,
            String path
    ) {}
}
