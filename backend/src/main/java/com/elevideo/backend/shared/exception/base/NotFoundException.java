package com.elevideo.backend.shared.exception.base;

/**
 * Excepción base para recursos no encontrados.
 * Cada módulo crea su propia excepción específica extendiendo esta.
 * Ejemplo: VideoNotFoundException, ProjectNotFoundException, UserNotFoundException.
 */
public abstract class NotFoundException extends DomainException {

    protected NotFoundException(String message) {
        super(message);
    }
}