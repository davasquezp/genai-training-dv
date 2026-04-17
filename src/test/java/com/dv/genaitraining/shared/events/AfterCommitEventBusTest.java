package com.dv.genaitraining.shared.events;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AfterCommitEventBusTest {
  record TestEvent(String value) implements DomainEvent {}

  @Autowired EventBus eventBus;
  @Autowired PlatformTransactionManager txManager;

  @Test
  void publish_inside_transaction_is_deferred_until_commit() {
    List<TestEvent> seen = new ArrayList<>();
    eventBus.subscribe(TestEvent.class, seen::add);

    TransactionTemplate tt = new TransactionTemplate(txManager);
    tt.executeWithoutResult(status -> {
      eventBus.publish(new TestEvent("x"));
      assertThat(seen).isEmpty();
    });

    assertThat(seen).hasSize(1);
    assertThat(seen.getFirst().value()).isEqualTo("x");
  }
}

