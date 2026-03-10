package com.elevideo.backend.user.internal;

import com.elevideo.backend.shared.exception.base.DomainException;

public class InvalidCurrentPasswordException extends DomainException {
    public InvalidCurrentPasswordException() {
        super("Current password is incorrect.");
    }
}
