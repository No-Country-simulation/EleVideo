package com.elevideo.backend.video.api.dto;

import com.elevideo.backend.video.internal.model.VideoStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Schema(
        name = "Video.VideoSearchRequest",
        description = "Parámetros de búsqueda, filtrado y paginación para videos."
)
public record VideoSearchRequest(

        @Schema(description = "Búsqueda parcial por título (case-insensitive).", example = "tutorial")
        String searchTerm,

        @Schema(description = "Filtrar por estado.", allowableValues = {"UPLOADED", "PROCESSING", "COMPLETED", "FAILED"})
        VideoStatus status,

        @Schema(description = "Página (0-indexed).", defaultValue = "0")
        Integer page,

        @Schema(description = "Elementos por página.", defaultValue = "20")
        Integer size,

        @Schema(description = "Campo de ordenamiento.", defaultValue = "createdAt",
                allowableValues = {"createdAt", "updatedAt", "title", "durationInMillis", "sizeInBytes", "status"})
        String sortBy,

        @Schema(description = "Dirección.", defaultValue = "DESC", allowableValues = {"ASC", "DESC"})
        String sortDirection
) {

    public Pageable toPageable() {
        int pageNumber = (page != null && page >= 0) ? page : 0;
        int pageSize   = (size != null && size > 0)  ? size : 20;

        String sortField       = (sortBy != null && !sortBy.isBlank()) ? sortBy : "createdAt";
        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField));
    }
}
