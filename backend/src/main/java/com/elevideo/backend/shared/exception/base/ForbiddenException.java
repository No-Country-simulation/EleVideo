package com.elevideo.backend.shared.exception.base;

/**
 * Excepción base para accesos no autorizados a recursos de otro usuario.
 */
public abstract class ForbiddenException extends DomainException {

    protected ForbiddenException(String message) {
        super(message);
    }
}