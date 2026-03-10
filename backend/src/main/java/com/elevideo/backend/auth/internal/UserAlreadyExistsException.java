package com.elevideo.backend.auth.internal;

import com.elevideo.backend.shared.exception.base.ConflictException;

public class UserAlreadyExistsException extends ConflictException {

    public UserAlreadyExistsException(String email) {
        super("El email ya está registrado: " + email);
    }
}
