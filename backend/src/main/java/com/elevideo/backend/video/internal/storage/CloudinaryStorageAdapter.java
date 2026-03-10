package com.elevideo.backend.video.internal.storage;

import com.cloudinary.Cloudinary;
import com.elevideo.backend.shared.exception.base.DomainException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Adaptador Cloudinary para el puerto VideoStoragePort.
 * Visibilidad de paquete — nadie fuera de video/internal lo inyecta.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class CloudinaryStorageAdapter implements VideoStoragePort {

    private final Cloudinary cloudinary;

    @Override
    public CloudinaryUploadResponse upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo de video está vacío");
        }

        try {
            Map<String, Object> result = cloudinary.uploader().uploadLarge(
                    file.getBytes(),
                    Map.of(
                            "resource_type", "video",
                            "folder",        "Elevideo"
                    )
            );
            log.info("☁️ Video subido a Cloudinary. publicId: {}", result.get("public_id"));
            return toUploadRes(result);

        } catch (Exception e) {
            throw new CloudinaryUploadException("Error al subir el video: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            throw new IllegalArgumentException("publicId no puede ser null o vacío");
        }

        try {
            Map<String, Object> result = cloudinary.uploader().destroy(
                    publicId,
                    Map.of("resource_type", "video")
            );

            String outcome = result.get("result").toString();
            if (!"ok".equals(outcome)) {
                throw new CloudinaryUploadException("No se pudo eliminar el video. Resultado: " + outcome);
            }
            log.info("🗑️ Video eliminado de Cloudinary. publicId: {}", publicId);

        } catch (Exception e) {
            throw new CloudinaryUploadException("Error al eliminar el video: " + e.getMessage(), e);
        }
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    private CloudinaryUploadResponse toUploadRes(Map<String, Object> result) {
        return CloudinaryUploadResponse.builder()
                .publicId(          (String)  result.get("public_id"))
                .secureUrl(         (String)  result.get("secure_url"))
                .format(            (String)  result.get("format"))
                .durationInSeconds( toDouble(result.get("duration")))
                .sizeInBytes(       toLong(   result.get("bytes")))
                .width(             toInt(    result.get("width")))
                .height(            toInt(    result.get("height")))
                .resourceType(      (String)  result.get("resource_type"))
                .build();
    }

    private Double  toDouble(Object v) { return v == null ? null : Double.valueOf(v.toString()); }
    private Long    toLong(Object v)   { return v == null ? null : Long.valueOf(v.toString()); }
    private Integer toInt(Object v)    { return v == null ? null : Integer.valueOf(v.toString()); }

    // ----------------------------------------------------------------
    // Excepción interna de storage
    // ----------------------------------------------------------------

    static class CloudinaryUploadException extends DomainException {
        CloudinaryUploadException(String message)                  { super(message); }
        CloudinaryUploadException(String message, Throwable cause) { super(message, cause); }
    }
}
