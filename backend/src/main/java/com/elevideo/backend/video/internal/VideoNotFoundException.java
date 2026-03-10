package com.elevideo.backend.video.internal;

import com.elevideo.backend.shared.exception.base.NotFoundException;

public class VideoNotFoundException extends NotFoundException {

    public VideoNotFoundException(Long id) {
        super("Video no encontrado con id: " + id);
    }
}
