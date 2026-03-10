package com.elevideo.backend.notification.internal.dto;

/**
 * DTO que representa el cuerpo del request a la API de Resend.
 */
public record EmailRequest(
        String from,
        String to,
        String subject,
        String html
) {}
