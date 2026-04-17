package com.dv.genaitraining.shared.events;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * In-memory event bus implementation.
 */
public class InMemoryEventBus implements EventBus {
  private final Map<Class<?>, List<EventHandler<?>>> handlersByType = new ConcurrentHashMap<>();

  @Override
  public void publish(DomainEvent event) {
    Objects.requireNonNull(event, "event");
    List<EventHandler<?>> handlers = handlersByType.getOrDefault(event.getClass(), List.of());
    for (EventHandler<?> h : handlers) {
      @SuppressWarnings("unchecked")
      EventHandler<DomainEvent> cast = (EventHandler<DomainEvent>) h;
      cast.handle(event);
    }
  }

  @Override
  public <T extends DomainEvent> void subscribe(Class<T> eventType, EventHandler<T> handler) {
    Objects.requireNonNull(eventType, "eventType");
    Objects.requireNonNull(handler, "handler");
    handlersByType.computeIfAbsent(eventType, ignored -> new CopyOnWriteArrayList<>()).add(handler);
  }
}

