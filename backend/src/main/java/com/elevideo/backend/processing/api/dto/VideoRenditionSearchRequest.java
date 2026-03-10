package com.elevideo.backend.processing.api.dto;

import com.elevideo.backend.processing.internal.model.BackgroundMode;
import com.elevideo.backend.processing.internal.model.Platform;
import com.elevideo.backend.processing.internal.model.ProcessingMode;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Schema(name = "VideoRendition.VideoRenditionSearchRequest")
public record VideoRenditionSearchRequest(
        ProcessingMode processingMode,
        Platform       platform,
        BackgroundMode backgroundMode,
        Integer        page,
        Integer        size,
        String         sortBy,
        String         sortDirection
) {
    public Pageable toPageable() {
        int pageNumber = (page != null && page >= 0) ? page : 0;
        int pageSize   = (size != null && size > 0)  ? size : 20;
        String field   = (sortBy != null && !sortBy.isBlank()) ? sortBy : "createdAt";
        Sort.Direction dir = "ASC".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(pageNumber, pageSize, Sort.by(dir, field));
    }
}
