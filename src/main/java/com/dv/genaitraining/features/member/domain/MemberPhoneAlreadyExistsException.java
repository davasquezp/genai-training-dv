package com.dv.genaitraining.features.member.domain;

/**
 * Thrown when phone uniqueness is violated.
 */
public class MemberPhoneAlreadyExistsException extends RuntimeException {
  public MemberPhoneAlreadyExistsException(String phone) {
    super("Member with phone already exists: " + phone);
  }
}

