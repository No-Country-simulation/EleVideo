package com.elevideo.backend.shared.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Clase base para todos los eventos de dominio internos del monolito.
 * Los eventos se publican y consumen exclusivamente mediante Spring Events.
 */
public abstract class DomainEvent {

    private final UUID eventId;
    private final Instant occurredAt;

    protected DomainEvent() {
        this.eventId    = UUID.randomUUID();
        this.occurredAt = Instant.now();
    }

    public UUID getEventId() {
        return eventId;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}