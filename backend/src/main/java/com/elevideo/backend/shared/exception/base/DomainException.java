package com.elevideo.backend.shared.exception.base;

/**
 * Excepción base para todas las excepciones de dominio del sistema.
 * Permite al GlobalExceptionHandler manejar cualquier excepción de negocio
 * de forma genérica sin acoplarse a excepciones específicas.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}