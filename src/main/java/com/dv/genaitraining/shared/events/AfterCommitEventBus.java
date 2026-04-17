package com.dv.genaitraining.shared.events;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Event bus decorator that publishes after transaction commit when a transaction is active.
 */
public class AfterCommitEventBus implements EventBus {
  private final EventBus delegate;

  public AfterCommitEventBus(EventBus delegate) {
    this.delegate = Objects.requireNonNull(delegate, "delegate");
  }

  @Override
  public void publish(DomainEvent event) {
    Objects.requireNonNull(event, "event");
    if (!TransactionSynchronizationManager.isSynchronizationActive()) {
      delegate.publish(event);
      return;
    }
    TransactionSynchronizationManager.registerSynchronization(new PublishAfterCommit(List.of(event)));
  }

  @Override
  public void publishAll(List<? extends DomainEvent> events) {
    Objects.requireNonNull(events, "events");
    if (events.isEmpty()) return;
    if (!TransactionSynchronizationManager.isSynchronizationActive()) {
      delegate.publishAll(events);
      return;
    }
    List<DomainEvent> copy = new ArrayList<>(events);
    TransactionSynchronizationManager.registerSynchronization(new PublishAfterCommit(copy));
  }

  @Override
  public <T extends DomainEvent> void subscribe(Class<T> eventType, EventHandler<T> handler) {
    delegate.subscribe(eventType, handler);
  }

  private class PublishAfterCommit implements TransactionSynchronization {
    private final List<DomainEvent> events;

    private PublishAfterCommit(List<DomainEvent> events) {
      this.events = events;
    }

    @Override
    public void afterCommit() {
      delegate.publishAll(events);
    }
  }
}

