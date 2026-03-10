package com.elevideo.backend.processing.internal.mapper;

import com.elevideo.backend.processing.api.dto.*;
import com.elevideo.backend.processing.api.dto.webhook.ProcessingJobWebhookRequest;
import com.elevideo.backend.processing.internal.model.JobStatus;
import com.elevideo.backend.processing.internal.model.ProcessingJob;
import com.elevideo.backend.processing.internal.model.VideoRendition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring", imports = JobStatus.class)
public interface ProcessingJobMapper {

    @Mapping(target = "jobId",          source = "response.jobId")
    @Mapping(target = "status",         source = "response.status")
    @Mapping(target = "processingMode", source = "request.processingMode")
    @Mapping(target = "platform",       source = "request.platform")
    @Mapping(target = "quality",        source = "request.quality")
    @Mapping(target = "backgroundMode", source = "request.backgroundMode")
    ProcessingJob toProcessingJob(VideoProcessRequest request, VideoJobCreatedResponse response);

    /**
     * Mapea la respuesta en tiempo real de Python al DTO unificado.
     * processingMode, platform, backgroundMode vienen del job local, no de Python —
     * se ignoran aquí y se enriquecen en ProcessingServiceImpl tras la llamada.
     */
    @Mapping(target = "status",              expression = "java(JobStatus.fromValue(response.status()))")
    @Mapping(target = "output.videoUrl",     source = "outputUrl")
    @Mapping(target = "output.thumbnailUrl", source = "thumbnailUrl")
    @Mapping(target = "output.previewUrl",   source = "previewUrl")
    @Mapping(target = "processingMode",      ignore = true)
    @Mapping(target = "platform",            ignore = true)
    @Mapping(target = "backgroundMode",      ignore = true)
    @Mapping(target = "quality",             ignore = true)
    @Mapping(target = "errorDetail",         source = "errorDetail")
    @Mapping(target = "createdAt",           source = "createdAt")
    JobResponse toJobResponse(VideoJobStatusResponse response);

    /**
     * Mapea una entidad local ProcessingJob al DTO unificado.
     * - output se extrae de videoRendition si existe (job COMPLETED).
     * - errorDetail se extrae de errorMessage si existe (job FAILED).
     * - progress mapea progressPercent → progress.
     */
    @Mapping(target = "progress",     source = "progressPercent")
    @Mapping(target = "errorDetail",  source = "errorMessage")
    @Mapping(target = "output",       source = "videoRendition", qualifiedByName = "renditionToOutput")
    JobResponse toJobResponse(ProcessingJob entity);

    void updateFromWebhook(ProcessingJobWebhookRequest request, @MappingTarget ProcessingJob job);

    /**
     * Convierte un VideoRendition en el JobOutput del DTO.
     * Devuelve null si la rendition es null (job no completado aún).
     */
    @Named("renditionToOutput")
    default JobResponse.JobOutput renditionToOutput(VideoRendition rendition) {
        if (rendition == null) return null;
        return new JobResponse.JobOutput(
                rendition.getOutputUrl(),
                rendition.getThumbnailUrl(),
                rendition.getPreviewUrl()
        );
    }

    default LocalDateTime map(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}