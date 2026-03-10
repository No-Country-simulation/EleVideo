package com.elevideo.backend.auth.internal.event;

import com.elevideo.backend.shared.event.DomainEvent;

import java.util.UUID;

/**
 * Evento publicado cuando un nuevo usuario completa el registro.
 * Consumidores: notification (envío de email de verificación).
 */
public class UserRegisteredEvent extends DomainEvent {

    private final UUID   userId;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String verificationToken;

    public UserRegisteredEvent(UUID userId, String email, String firstName,
                                String lastName, String verificationToken) {
        super();
        this.userId            = userId;
        this.email             = email;
        this.firstName         = firstName;
        this.lastName          = lastName;
        this.verificationToken = verificationToken;
    }

    public UUID   getUserId()            { return userId; }
    public String getEmail()             { return email; }
    public String getFirstName()         { return firstName; }
    public String getLastName()          { return lastName; }
    public String getVerificationToken() { return verificationToken; }
}
