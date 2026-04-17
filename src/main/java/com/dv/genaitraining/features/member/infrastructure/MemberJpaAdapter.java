package com.dv.genaitraining.features.member.infrastructure;

import com.dv.genaitraining.features.member.domain.Member;
import com.dv.genaitraining.features.member.domain.MemberRepository;
import com.dv.genaitraining.shared.ids.MemberId;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

/**
 * JPA-backed adapter implementing {@link MemberRepository}.
 */
@Repository
public class MemberJpaAdapter implements MemberRepository {
  private final MemberSpringDataRepository springDataRepository;

  public MemberJpaAdapter(MemberSpringDataRepository springDataRepository) {
    this.springDataRepository = Objects.requireNonNull(springDataRepository, "springDataRepository");
  }

  @Override
  public Member save(Member member) {
    MemberJpaEntity saved = springDataRepository.save(toEntity(member));
    return toDomain(saved);
  }

  @Override
  public Optional<Member> findByEmail(String email) {
    return springDataRepository.findByEmail(email).map(MemberJpaAdapter::toDomain);
  }

  @Override
  public Optional<Member> findByPhone(String phone) {
    return springDataRepository.findByPhone(phone).map(MemberJpaAdapter::toDomain);
  }

  @Override
  public Optional<Member> findById(MemberId id) {
    return springDataRepository.findById(id.value()).map(MemberJpaAdapter::toDomain);
  }

  private static Member toDomain(MemberJpaEntity e) {
    return new Member(
        new MemberId(e.getId()),
        e.getEmail(),
        e.getName(),
        e.getPhone(),
        e.getPasswordHash(),
        e.getPasswordSalt(),
        e.getCountryCode(),
        e.getCountryName(),
        e.getNationalityCode(),
        e.getNationalityName(),
        e.getRoles(),
        e.getCreatedAt()
    );
  }

  private static MemberJpaEntity toEntity(Member m) {
    return new MemberJpaEntity(
        m.id().value(),
        m.email(),
        m.name(),
        m.phone(),
        m.passwordHash(),
        m.passwordSalt(),
        m.countryCode(),
        m.countryName(),
        m.nationalityCode(),
        m.nationalityName(),
        m.roles(),
        m.createdAt()
    );
  }
}