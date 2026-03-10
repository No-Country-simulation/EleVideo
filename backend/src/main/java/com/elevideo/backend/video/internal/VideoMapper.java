package com.elevideo.backend.video.internal;

import com.elevideo.backend.video.api.dto.VideoResponse;
import com.elevideo.backend.video.internal.model.Video;
import com.elevideo.backend.video.internal.model.VideoStatus;
import com.elevideo.backend.video.internal.storage.CloudinaryUploadResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = VideoStatus.class)
interface VideoMapper {

    @Mapping(target = "status",          expression = "java(VideoStatus.UPLOADED)")
    @Mapping(target = "durationInSeconds", expression = "java(uploadRes.durationInSeconds() == null ? null : uploadRes.durationInSeconds().longValue())")
    @Mapping(target = "title",           source = "title")
    Video toVideo(String title, CloudinaryUploadResponse uploadRes);

    /**
     * secureUrl (campo interno de infraestructura) → videoUrl (campo público de la API).
     * Desacopla el contrato de la API del proveedor de storage.
     */
    @Mapping(target = "videoUrl", source = "secureUrl")
    VideoResponse toVideoResponse(Video video);
}
