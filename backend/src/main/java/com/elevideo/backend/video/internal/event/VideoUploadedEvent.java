package com.elevideo.backend.video.internal.event;

import com.elevideo.backend.shared.event.DomainEvent;

/**
 * Evento publicado cuando un video es subido exitosamente.
 * Reservado para uso futuro (ej: disparar procesamiento automático,
 * notificar al usuario, analytics, etc.)
 */
public class VideoUploadedEvent extends DomainEvent {

    private final Long   videoId;
    private final Long   projectId;
    private final String title;
    private final String publicId;

    public VideoUploadedEvent(Long videoId, Long projectId, String title, String publicId) {
        super();
        this.videoId   = videoId;
        this.projectId = projectId;
        this.title     = title;
        this.publicId  = publicId;
    }

    public Long   getVideoId()   { return videoId; }
    public Long   getProjectId() { return projectId; }
    public String getTitle()     { return title; }
    public String getPublicId()  { return publicId; }
}
