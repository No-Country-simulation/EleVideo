package com.elevideo.backend.processing.internal;

import com.elevideo.backend.processing.api.dto.webhook.ProcessingJobWebhookRequest;
import com.elevideo.backend.processing.api.dto.webhook.ProgressWebhookRequest;
import com.elevideo.backend.processing.internal.event.VideoProcessingCompletedEvent;
import com.elevideo.backend.processing.internal.mapper.ProcessingJobMapper;
import com.elevideo.backend.processing.internal.mapper.VideoRenditionMapper;
import com.elevideo.backend.processing.internal.model.JobStatus;
import com.elevideo.backend.processing.internal.model.ProcessingJob;
import com.elevideo.backend.processing.internal.model.VideoRendition;
import com.elevideo.backend.processing.internal.repository.ProcessingJobRepository;
import com.elevideo.backend.processing.internal.repository.VideoRenditionRepository;
import com.elevideo.backend.shared.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio interno que procesa los webhooks recibidos desde el microservicio Python.
 * Visibilidad de paquete — solo WebhookController lo inyecta.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookHandlerService {

    private final ProcessingJobRepository  jobRepository;
    private final VideoRenditionRepository renditionRepository;
    private final ProcessingJobMapper      jobMapper;
    private final VideoRenditionMapper     renditionMapper;
    private final DomainEventPublisher     eventPublisher;

    @Transactional
    public void handleJobCompleted(ProcessingJobWebhookRequest request) {
        ProcessingJob job = jobRepository.findByJobId(request.jobId())
                .orElseThrow(() -> new IllegalArgumentException("Job no encontrado: " + request.jobId()));

        jobMapper.updateFromWebhook(request, job);

        if (request.isCompleted()) {
            job.setProgressPercent(100);
            VideoRendition rendition = createRendition(job, request);
            job.setVideoRendition(rendition);

            eventPublisher.publish(new VideoProcessingCompletedEvent(
                    job.getVideoId(), job.getJobId(), job.getProcessingMode(), request.outputUrl()
            ));
        }

        jobRepository.save(job);
    }

    @Transactional
    public void handleProgressUpdate(ProgressWebhookRequest request) {
        ProcessingJob job = jobRepository.findByJobId(request.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("Job no encontrado: " + request.getJobId()));

        job.setProgressPercent(request.getProgress());
        job.setPhase(request.getPhase());
        job.setEtaSeconds(request.getEtaSeconds());
        job.setMessage(request.getMessage());
        job.setStatus(JobStatus.fromValue(request.getStatus()));

        jobRepository.save(job);
    }

    // ----------------------------------------------------------------
    // Helpers privados
    // ----------------------------------------------------------------

    private VideoRendition createRendition(ProcessingJob job, ProcessingJobWebhookRequest request) {
        VideoRendition rendition = renditionMapper.toVideoRendition(request);
        rendition.setVideoId(job.getVideoId());
        rendition.setPlatform(job.getPlatform());
        rendition.setQuality(job.getQuality());
        rendition.setBackgroundMode(job.getBackgroundMode());
        rendition.setProcessingMode(job.getProcessingMode());
        return renditionRepository.save(rendition);
    }
}
