package com.dv.genaitraining.features.customer;

import java.util.Objects;

public record Customer(CustomerId id, String email) {
  public Customer {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(email, "email");
    if (email.isBlank()) {
      throw new IllegalArgumentException("email must not be blank");
    }
  }
}

