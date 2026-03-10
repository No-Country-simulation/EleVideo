package com.elevideo.backend.shared.web;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Estructura estándar de respuesta para todos los endpoints de la API.
 */
@Schema(description = "Estructura estándar de respuesta de la API")
public record ApiResult<T>(

        @Schema(description = "Indica si la operación fue exitosa", example = "true")
        boolean success,

        @Schema(description = "Mensaje descriptivo de la respuesta", example = "Operación exitosa")
        String message,

        @Schema(description = "Datos retornados por la API (null si no aplica)")
        T data

) {
    public static <T> ApiResult<T> success(T data, String message) {
        return new ApiResult<>(true, message, data);
    }

    public static <T> ApiResult<T> success(String message) {
        return new ApiResult<>(true, message, null);
    }
}