package com.elevideo.backend.processing.internal.event;

import com.elevideo.backend.processing.internal.model.ProcessingMode;
import com.elevideo.backend.shared.event.DomainEvent;

/**
 * Evento publicado cuando un job de procesamiento finaliza exitosamente.
 * Consumidores: notification (futura notificación al usuario).
 */
public class VideoProcessingCompletedEvent extends DomainEvent {

    private final Long          videoId;
    private final String        jobId;
    private final ProcessingMode processingMode;
    private final String        outputUrl;

    public VideoProcessingCompletedEvent(Long videoId, String jobId,
                                          ProcessingMode processingMode, String outputUrl) {
        super();
        this.videoId        = videoId;
        this.jobId          = jobId;
        this.processingMode = processingMode;
        this.outputUrl      = outputUrl;
    }

    public Long          getVideoId()        { return videoId; }
    public String        getJobId()          { return jobId; }
    public ProcessingMode getProcessingMode() { return processingMode; }
    public String        getOutputUrl()      { return outputUrl; }
}
