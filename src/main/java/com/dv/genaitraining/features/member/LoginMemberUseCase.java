package com.dv.genaitraining.features.member;

/**
 * Use-case for authenticating a member.
 */
public interface LoginMemberUseCase {
  Member login(String email, String password);
}

