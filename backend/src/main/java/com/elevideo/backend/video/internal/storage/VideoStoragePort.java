package com.elevideo.backend.video.internal.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * Puerto de almacenamiento de video.
 * La implementación concreta (Cloudinary) está en CloudinaryStorageAdapter.
 * Permite cambiar el proveedor de storage sin tocar VideoServiceImpl.
 */
public interface VideoStoragePort {

    CloudinaryUploadResponse upload(MultipartFile file);

    void delete(String publicId);
}
