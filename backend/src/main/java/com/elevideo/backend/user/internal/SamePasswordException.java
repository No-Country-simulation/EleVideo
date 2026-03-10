package com.elevideo.backend.user.internal;

import com.elevideo.backend.shared.exception.base.DomainException;

public class SamePasswordException extends DomainException {

    public SamePasswordException() {
        super("La nueva contraseña no puede ser igual a la contraseña actual.");
    }
}
