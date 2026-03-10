package com.elevideo.backend.shared.web;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Wrapper de paginación propio que reemplaza el Page de Spring Data en las respuestas HTTP.
 * Expone solo los campos que el frontend necesita, sin acoplar el contrato de la API
 * a los detalles internos de Spring Data.
 */
@Schema(description = "Respuesta paginada estándar")
public record PageResponse<T>(

        @Schema(description = "Lista de elementos en la página actual")
        List<T> content,

        @Schema(description = "Número de página actual (base 0)", example = "0")
        int page,

        @Schema(description = "Tamaño de página solicitado", example = "20")
        int size,

        @Schema(description = "Total de elementos en todas las páginas", example = "45")
        long totalElements,

        @Schema(description = "Total de páginas disponibles", example = "3")
        int totalPages,

        @Schema(description = "Indica si existe una página siguiente", example = "true")
        boolean hasNext,

        @Schema(description = "Indica si existe una página anterior", example = "false")
        boolean hasPrevious

) {
    /**
     * Construye un PageResponse a partir de un Page de Spring Data.
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}