package com.dv.genaitraining.features.member.domain;

import com.dv.genaitraining.shared.ids.MemberId;

import java.util.Optional;

/**
 * Outbound port for member persistence.
 */
public interface MemberRepository {
  /**
   * Saves a member.
   *
   * @param member member
   * @return saved member
   */
  Member save(Member member);

  /**
   * Finds a member by email.
   *
   * @param email email
   * @return member if present
   */
  Optional<Member> findByEmail(String email);

  /**
   * Finds a member by phone.
   *
   * @param phone phone
   * @return member if present
   */
  Optional<Member> findByPhone(String phone);

  /**
   * Finds a member by id.
   *
   * @param id id
   * @return member if present
   */
  Optional<Member> findById(MemberId id);
}

