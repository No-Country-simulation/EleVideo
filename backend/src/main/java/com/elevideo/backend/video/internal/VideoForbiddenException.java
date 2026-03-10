package com.elevideo.backend.video.internal;

import com.elevideo.backend.shared.exception.base.ForbiddenException;

public class VideoForbiddenException extends ForbiddenException {

    public VideoForbiddenException(Long videoId) {
        super("No tienes permiso para acceder al video con id: " + videoId);
    }
}
