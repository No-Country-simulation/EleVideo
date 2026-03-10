package com.elevideo.backend.user.internal.event;

import com.elevideo.backend.shared.event.DomainEvent;

import java.util.UUID;

/**
 * Evento publicado cuando un usuario verifica su correo electrónico.
 * Consumidores: notification (envío de email de bienvenida si aplica).
 */
public class UserEmailVerifiedEvent extends DomainEvent {

    private final UUID   userId;
    private final String email;

    public UserEmailVerifiedEvent(UUID userId, String email) {
        super();
        this.userId = userId;
        this.email  = email;
    }

    public UUID   getUserId() { return userId; }
    public String getEmail()  { return email; }
}
