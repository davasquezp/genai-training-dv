package com.dv.genaitraining.shared.events;

import java.util.List;

/**
 * Event bus abstraction.
 *
 * <p>Implementations may be in-memory, Kafka-backed, outbox-backed, etc.</p>
 */
public interface EventBus {
  /**
   * Publishes an event.
   *
   * @param event event
   */
  void publish(DomainEvent event);

  /**
   * Publishes multiple events.
   *
   * @param events events
   */
  default void publishAll(List<? extends DomainEvent> events) {
    for (DomainEvent e : events) {
      publish(e);
    }
  }

  /**
   * Subscribes a handler to an event type.
   *
   * @param eventType event type
   * @param handler handler
   * @param <T> event type
   */
  <T extends DomainEvent> void subscribe(Class<T> eventType, EventHandler<T> handler);
}

