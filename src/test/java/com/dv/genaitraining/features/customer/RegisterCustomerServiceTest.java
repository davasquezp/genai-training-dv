package com.dv.genaitraining.features.customer;

import com.dv.genaitraining.features.customer.Customer;
import com.dv.genaitraining.features.customer.CustomerId;
import com.dv.genaitraining.features.customer.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterCustomerServiceTest {
  @Mock CustomerRepository customerRepository;
  @Captor ArgumentCaptor<Customer> customerCaptor;

  @Test
  void returnsExistingCustomerWhenEmailAlreadyRegistered() {
    var existing = new Customer(new CustomerId(UUID.fromString("00000000-0000-0000-0000-000000000001")), "a@b.com");
    when(customerRepository.findByEmail("a@b.com")).thenReturn(Optional.of(existing));

    var service = new RegisterCustomerService(customerRepository);
    var result = service.register("a@b.com");

    assertThat(result).isSameAs(existing);
  }

  @Test
  void createsNewCustomerWhenEmailNotRegistered() {
    when(customerRepository.findByEmail("new@b.com")).thenReturn(Optional.empty());
    when(customerRepository.save(customerCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

    var service = new RegisterCustomerService(customerRepository);
    var result = service.register("new@b.com");

    verify(customerRepository).save(customerCaptor.getValue());
    assertThat(result.email()).isEqualTo("new@b.com");
    assertThat(result.id().value()).isNotNull();
  }
}

