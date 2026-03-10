package com.elevideo.backend.user.internal;

import com.elevideo.backend.shared.exception.base.ConflictException;

public class EmailAlreadyVerifiedException extends ConflictException {

    public EmailAlreadyVerifiedException() {
        super("El email ya ha sido verificado previamente.");
    }
}
