package com.dv.genaitraining.shared.events;

/**
 * Handler for a specific domain event type.
 *
 * @param <T> event type
 */
@FunctionalInterface
public interface EventHandler<T extends DomainEvent> {
  /**
   * Handles an event.
   *
   * @param event event
   */
  void handle(T event);
}

