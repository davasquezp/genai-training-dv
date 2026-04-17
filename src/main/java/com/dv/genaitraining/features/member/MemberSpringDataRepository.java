package com.dv.genaitraining.features.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data repository for {@link MemberJpaEntity}.
 */
public interface MemberSpringDataRepository extends JpaRepository<MemberJpaEntity, UUID> {
  Optional<MemberJpaEntity> findByEmail(String email);

  Optional<MemberJpaEntity> findByPhone(String phone);
}

