package com.elevideo.backend.project.internal;

import com.elevideo.backend.shared.exception.base.NotFoundException;

public class ProjectNotFoundException extends NotFoundException {

    public ProjectNotFoundException(Long id) {
        super("Proyecto no encontrado con id: " + id);
    }
}
