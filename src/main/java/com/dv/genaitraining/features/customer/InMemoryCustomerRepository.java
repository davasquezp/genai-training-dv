package com.dv.genaitraining.features.customer;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCustomerRepository implements CustomerRepository {
  private final Map<String, Customer> byEmail = new ConcurrentHashMap<>();

  @Override
  public Optional<Customer> findByEmail(String email) {
    return Optional.ofNullable(byEmail.get(email));
  }

  @Override
  public Customer save(Customer customer) {
    byEmail.put(customer.email(), customer);
    return customer;
  }
}

