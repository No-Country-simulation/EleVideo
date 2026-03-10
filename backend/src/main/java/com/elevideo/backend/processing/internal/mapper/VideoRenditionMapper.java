package com.elevideo.backend.processing.internal.mapper;

import com.elevideo.backend.processing.api.dto.VideoRenditionResponse;
import com.elevideo.backend.processing.api.dto.webhook.ProcessingJobWebhookRequest;
import com.elevideo.backend.processing.internal.model.VideoRendition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VideoRenditionMapper {

    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "createdAt",   ignore = true)
    @Mapping(target = "processingMode", source = "processingMode")
    @Mapping(target = "durationSeconds", source = "outputDurationSeconds")
    VideoRendition toVideoRendition(ProcessingJobWebhookRequest request);

    VideoRenditionResponse toVideoRenditionResponse(VideoRendition entity);

    List<VideoRenditionResponse> toVideoRenditionResponseList(List<VideoRendition> entities);
}
