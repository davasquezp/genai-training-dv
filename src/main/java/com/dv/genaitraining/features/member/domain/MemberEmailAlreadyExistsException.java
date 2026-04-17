package com.dv.genaitraining.features.member.domain;

/**
 * Thrown when email uniqueness is violated.
 */
public class MemberEmailAlreadyExistsException extends RuntimeException {
  public MemberEmailAlreadyExistsException(String email) {
    super("Member with email already exists: " + email);
  }
}

