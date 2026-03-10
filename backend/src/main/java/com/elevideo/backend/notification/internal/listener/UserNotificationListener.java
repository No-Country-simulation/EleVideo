package com.elevideo.backend.notification.internal.listener;

import com.elevideo.backend.notification.internal.EmailSenderService;
import com.elevideo.backend.user.internal.event.UserEmailVerifiedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Escucha eventos del módulo user relacionados con la cuenta.
 *
 * Actualmente: reservado para acciones futuras tras verificación de email
 * (ej: enviar email de bienvenida post-verificación, activar onboarding, etc.)
 */
@Slf4j
@Component
@RequiredArgsConstructor
class UserNotificationListener {

    private final EmailSenderService emailSender;

    @Async
    @EventListener
    public void onUserEmailVerified(UserEmailVerifiedEvent event) {
        // Por ahora solo log. Aquí se puede añadir un email de bienvenida,
        // activación de features, notificación al equipo, etc.
        log.info("✅ Email verificado para usuario '{}' — notificación pendiente de implementar.",
                event.getEmail());
    }
}
