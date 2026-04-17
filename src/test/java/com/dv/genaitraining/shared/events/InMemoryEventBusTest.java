package com.dv.genaitraining.shared.events;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryEventBusTest {
  record TestEvent(String value) implements DomainEvent {}

  @Test
  void publish_dispatches_to_subscribers_of_exact_type() {
    InMemoryEventBus bus = new InMemoryEventBus();
    AtomicInteger seen = new AtomicInteger(0);
    bus.subscribe(TestEvent.class, e -> {
      assertThat(e.value()).isEqualTo("x");
      seen.incrementAndGet();
    });

    bus.publish(new TestEvent("x"));

    assertThat(seen.get()).isEqualTo(1);
  }
}

