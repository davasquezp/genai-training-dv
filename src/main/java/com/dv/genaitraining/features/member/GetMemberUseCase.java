package com.dv.genaitraining.features.member;

import com.dv.genaitraining.shared.ids.MemberId;

import java.util.Optional;
import java.util.UUID;

/**
 * Use-case for retrieving members.
 */
public interface GetMemberUseCase {
  Optional<Member> get(MemberId id);
}

