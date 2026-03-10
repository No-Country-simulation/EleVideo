package com.elevideo.backend.video.api;

import com.elevideo.backend.video.api.dto.CreateVideoRequest;
import com.elevideo.backend.video.api.dto.VideoResponse;
import com.elevideo.backend.video.api.dto.VideoSearchRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

/**
 * API pública del módulo video.
 * Solo esta interfaz puede ser usada por otros módulos (ej: processing).
 */
public interface VideoService {

    /**
     * Sube un video a Cloudinary y lo asocia al proyecto indicado.
     */
    VideoResponse createVideo(Long projectId, CreateVideoRequest request);

    /**
     * Retorna los videos de un proyecto con paginación y filtros opcionales.
     */
    Page<VideoResponse> getVideos(Long projectId, VideoSearchRequest searchParams);

    /**
     * Retorna el detalle de un video validando que pertenezca al usuario autenticado.
     */
    VideoResponse getVideoById(Long videoId);

    /**
     * Elimina un video de Cloudinary y de la base de datos.
     */
    void deleteVideo(Long videoId);

    /**
     * Verifica que un video exista y pertenezca al usuario indicado.
     * Usado internamente por el módulo processing antes de lanzar un job.
     */
    void assertVideoOwnedByUser(Long videoId, UUID userId);
}
