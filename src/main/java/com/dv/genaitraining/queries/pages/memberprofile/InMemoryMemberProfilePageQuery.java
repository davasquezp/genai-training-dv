package com.dv.genaitraining.queries.pages.memberprofile;

import com.dv.genaitraining.features.dancer.domain.Dancer;
import com.dv.genaitraining.features.dancer.domain.DancerRepository;
import com.dv.genaitraining.features.member.domain.Member;
import com.dv.genaitraining.features.member.domain.MemberRepository;
import com.dv.genaitraining.shared.ids.MemberId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InMemoryMemberProfilePageQuery implements MemberProfilePageQuery {
  private final MemberRepository memberRepository;
  private final DancerRepository dancerRepository;

  public InMemoryMemberProfilePageQuery(MemberRepository memberRepository, DancerRepository dancerRepository) {
    this.memberRepository = memberRepository;
    this.dancerRepository = dancerRepository;
  }

  @Override
  public MemberProfilePageView load(org.springframework.security.core.Authentication authentication) {
    if (authentication == null || authentication.getPrincipal() == null) {
      throw new IllegalArgumentException("Unauthorized");
    }

    UUID memberUuid;
    try {
      memberUuid = UUID.fromString(String.valueOf(authentication.getPrincipal()));
    } catch (Exception e) {
      throw new IllegalArgumentException("Unauthorized");
    }

    Member member = memberRepository.findById(new MemberId(memberUuid)).orElse(null);
    if (member == null) throw new IllegalArgumentException("Unauthorized");

    Dancer dancer = dancerRepository.findByMemberId(new MemberId(memberUuid)).orElse(null);

    return new MemberProfilePageView(
        toMember(member),
        dancer == null ? null : toDancer(dancer)
    );
  }

  private static MemberProfilePageView.Member toMember(Member m) {
    return new MemberProfilePageView.Member(
        m.id().value(),
        m.email(),
        m.name(),
        m.phone(),
        m.countryCode(),
        m.countryName(),
        m.nationalityCode(),
        m.nationalityName(),
        m.roles(),
        m.createdAt()
    );
  }

  private static MemberProfilePageView.Dancer toDancer(Dancer d) {
    return new MemberProfilePageView.Dancer(
        d.id().value(),
        d.name(),
        d.roles() == null ? List.of() : d.roles().stream().map(Enum::name).toList()
    );
  }
}

