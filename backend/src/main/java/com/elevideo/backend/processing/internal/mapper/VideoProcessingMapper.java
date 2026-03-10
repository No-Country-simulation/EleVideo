package com.elevideo.backend.processing.internal.mapper;

import com.elevideo.backend.processing.api.dto.VideoProcessRequest;
import com.elevideo.backend.processing.internal.client.VideoPythonRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VideoProcessingMapper {

    @Mapping(target = "cloudinaryInputUrl", source = "videoSecureUrl")
    @Mapping(target = "platform",           expression = "java(request.platform().getValue())")
    @Mapping(target = "quality",            expression = "java(request.quality().getValue())")
    @Mapping(target = "backgroundMode",     expression = "java(request.backgroundMode().getValue())")
    @Mapping(target = "processingMode",     expression = "java(request.processingMode().getValue())")
    @Mapping(target = "shortOptions",       source = "request.shortOptions")
    @Mapping(target = "shortAutoDuration",  source = "request.shortAutoDuration")
    @Mapping(target = "advancedOptions",    source = "request.advancedOptions")
    VideoPythonRequest toVideoPythonRequest(VideoProcessRequest request, String videoSecureUrl);

    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "duration",  source = "duration")
    VideoPythonRequest.ShortOptionsDto toShortOptionsDto(VideoProcessRequest.ShortManualOptions options);
}
