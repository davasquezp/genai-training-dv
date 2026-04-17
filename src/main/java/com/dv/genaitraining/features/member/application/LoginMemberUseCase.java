package com.dv.genaitraining.features.member.application;

import com.dv.genaitraining.features.member.domain.Member;

/**
 * Use-case for authenticating a member.
 */
public interface LoginMemberUseCase {
  Member login(String email, String password);
}

