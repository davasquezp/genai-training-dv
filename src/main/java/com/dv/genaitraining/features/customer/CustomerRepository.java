package com.dv.genaitraining.features.customer;

import java.util.Optional;

public interface CustomerRepository {
  Optional<Customer> findByEmail(String email);

  Customer save(Customer customer);
}

