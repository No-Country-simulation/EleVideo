package com.elevideo.backend.auth.internal.event;

import com.elevideo.backend.shared.event.DomainEvent;

import java.util.UUID;

/**
 * Evento publicado cuando un usuario solicita recuperar su contraseña.
 * Consumidores: notification (envío de email con link de reset).
 */
public class PasswordResetRequestedEvent extends DomainEvent {

    private final UUID   userId;
    private final String email;
    private final String firstName;
    private final String resetToken;

    public PasswordResetRequestedEvent(UUID userId, String email,
                                        String firstName, String resetToken) {
        super();
        this.userId     = userId;
        this.email      = email;
        this.firstName  = firstName;
        this.resetToken = resetToken;
    }

    public UUID   getUserId()    { return userId; }
    public String getEmail()     { return email; }
    public String getFirstName() { return firstName; }
    public String getResetToken() { return resetToken; }
}
