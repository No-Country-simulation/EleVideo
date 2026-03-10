package com.elevideo.backend.user.internal;

import com.elevideo.backend.shared.exception.base.NotFoundException;

import java.util.UUID;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(UUID id) {
        super("Usuario no encontrado con id: " + id);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
