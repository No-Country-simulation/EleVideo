package com.elevideo.backend.processing.internal.client;

import com.elevideo.backend.shared.exception.base.DomainException;

public class PythonServiceException extends DomainException {

    public PythonServiceException(String message) { super(message); }

    public PythonServiceException(String message, Throwable cause) { super(message, cause); }
}
