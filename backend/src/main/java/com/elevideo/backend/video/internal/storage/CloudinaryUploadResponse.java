package com.elevideo.backend.video.internal.storage;

import lombok.Builder;

/**
 * Resultado del upload a Cloudinary.
 * DTO interno del módulo video — no sale de aquí.
 */
@Builder
public record CloudinaryUploadResponse(
        String  publicId,
        String  secureUrl,
        String  format,
        Long    sizeInBytes,
        Double  durationInSeconds,
        Integer width,
        Integer height,
        String  resourceType
) {}
