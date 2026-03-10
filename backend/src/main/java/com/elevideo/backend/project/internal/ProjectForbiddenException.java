package com.elevideo.backend.project.internal;

import com.elevideo.backend.shared.exception.base.ForbiddenException;

public class ProjectForbiddenException extends ForbiddenException {

    public ProjectForbiddenException(Long projectId) {
        super("No tienes permiso para acceder al proyecto con id: " + projectId);
    }
}
