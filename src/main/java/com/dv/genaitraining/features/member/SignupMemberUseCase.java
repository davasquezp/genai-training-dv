package com.dv.genaitraining.features.member;

/**
 * Use-case for signing up a new member.
 */
public interface SignupMemberUseCase {
  Member signup(String email, String name, String phone, String password);
}

