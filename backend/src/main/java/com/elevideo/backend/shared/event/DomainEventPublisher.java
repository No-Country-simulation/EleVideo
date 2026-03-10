 package com.elevideo.backend.shared.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Wrapper sobre ApplicationEventPublisher.
 * Centraliza la publicación de eventos de dominio para que los módulos
 * no dependan directamente de la infraestructura de Spring.
 */
@Component
@RequiredArgsConstructor
public class DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publish(DomainEvent event) {
        publisher.publishEvent(event);
    }
}