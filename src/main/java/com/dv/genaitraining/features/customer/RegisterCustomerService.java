package com.dv.genaitraining.features.customer;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RegisterCustomerService implements RegisterCustomerUseCase {
  private final CustomerRepository customerRepository;

  public RegisterCustomerService(CustomerRepository customerRepository) {
    this.customerRepository = Objects.requireNonNull(customerRepository, "customerRepository");
  }

  @Override
  public Customer register(String email) {
    Objects.requireNonNull(email, "email");

    return customerRepository
        .findByEmail(email)
        .orElseGet(() -> customerRepository.save(new Customer(CustomerId.newId(), email)));
  }
}

