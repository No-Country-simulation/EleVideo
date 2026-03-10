package com.elevideo.backend.shared.exception.base;

/**
 * Excepción base para conflictos de estado o datos duplicados.
 * Ejemplo: email ya registrado, recurso ya existente.
 */
public abstract class ConflictException extends DomainException {

    protected ConflictException(String message) {
        super(message);
    }
}