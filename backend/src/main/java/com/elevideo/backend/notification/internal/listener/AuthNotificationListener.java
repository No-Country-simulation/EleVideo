package com.elevideo.backend.notification.internal.listener;

import com.elevideo.backend.auth.internal.event.PasswordResetRequestedEvent;
import com.elevideo.backend.auth.internal.event.UserRegisteredEvent;
import com.elevideo.backend.notification.internal.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Escucha eventos del módulo auth y envía los correos correspondientes.
 * No conoce ni llama a auth directamente — solo reacciona a sus eventos.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class AuthNotificationListener {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final EmailSenderService emailSender;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    @Value("${app.frontend.verify-email-path}")
    private String verifyEmailPath;

    @Value("${app.frontend.reset-password-path}")
    private String resetPasswordPath;

    // ----------------------------------------------------------------
    // Handlers
    // ----------------------------------------------------------------

    @Async
    @EventListener
    public void onUserRegistered(UserRegisteredEvent event) {
        log.info("📬 Enviando email de verificación a '{}'", event.getEmail());

        String verificationUrl = buildUrl(frontendBaseUrl, verifyEmailPath, event.getVerificationToken());
        String fullName        = event.getFirstName() + " " + event.getLastName();
        String formattedDate   = LocalDateTime.now().format(DATE_FORMATTER);

        Map<String, Object> variables = Map.of(
                "userName",         fullName,
                "userEmail",        event.getEmail(),
                "registrationDate", formattedDate,
                "verificationLink", verificationUrl
        );

        emailSender.sendTemplate(
                event.getEmail(),
                "Verifica tu cuenta en Elevideo",
                "emails/welcome-email",
                variables
        );
    }

    @Async
    @EventListener
    public void onPasswordResetRequested(PasswordResetRequestedEvent event) {
        log.info("📬 Enviando email de recuperación de contraseña a '{}'", event.getEmail());

        String resetUrl = buildUrl(frontendBaseUrl, resetPasswordPath, event.getResetToken());

        Map<String, Object> variables = Map.of(
                "userName",  event.getFirstName(),
                "resetLink", resetUrl
        );

        emailSender.sendTemplate(
                event.getEmail(),
                "Recuperación de contraseña - Elevideo",
                "emails/password-reset",
                variables
        );
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    private String buildUrl(String baseUrl, String path, String token) {
        return UriComponentsBuilder
                .fromUriString(baseUrl)
                .path(path)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}
