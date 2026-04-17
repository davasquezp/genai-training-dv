package com.dv.genaitraining.features.member;

import com.dv.genaitraining.shared.ids.MemberId;
import com.dv.genaitraining.shared.events.EventBus;
import com.dv.genaitraining.shared.events.member.MemberSignedUp;
import com.dv.genaitraining.shared.events.member.MemberRoleAdded;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Application service for members.
 */
@Service
public class MemberService implements SignupMemberUseCase, LoginMemberUseCase, GetMemberUseCase {
  private final MemberRepository repository;
  private final EventBus eventBus;
  private final Clock clock;
  private final PasswordHasher passwordHasher;

  public MemberService(MemberRepository repository, EventBus eventBus, Clock clock) {
    this.repository = Objects.requireNonNull(repository, "repository");
    this.eventBus = Objects.requireNonNull(eventBus, "eventBus");
    this.clock = Objects.requireNonNull(clock, "clock");
    this.passwordHasher = new PasswordHasher();
  }

  @Override
  @Transactional
  public Member signup(String email, String name, String phone, String password) {
    Objects.requireNonNull(email, "email");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(phone, "phone");
    Objects.requireNonNull(password, "password");

    String normalizedEmail = email.trim().toLowerCase();
    String normalizedName = name.trim();
    String normalizedPhone = phone.trim();

    if (normalizedName.isBlank()) {
      throw new IllegalArgumentException("name must not be blank");
    }

    if (repository.findByEmail(normalizedEmail).isPresent()) {
      throw new MemberEmailAlreadyExistsException(normalizedEmail);
    }
    if (repository.findByPhone(normalizedPhone).isPresent()) {
      throw new MemberPhoneAlreadyExistsException(normalizedPhone);
    }

    PasswordHasher.HashedPassword hp = passwordHasher.hash(password.toCharArray());
    Member created = new Member(
        MemberId.newId(),
        normalizedEmail,
        normalizedName,
        normalizedPhone,
        hp.hashBase64(),
        hp.saltBase64(),
        null,
        null,
        null,
        null,
        Set.of(MemberRole.NONE),
        Instant.now(clock)
    );
    Member saved = repository.save(created);
    eventBus.publish(new MemberSignedUp(saved.id(), saved.roles()));
    return saved;
  }

  @Transactional
  public Member addRole(MemberId memberId, MemberRole role) {
    Objects.requireNonNull(memberId, "memberId");
    Objects.requireNonNull(role, "role");

    Member member = repository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
    if (member.roles().contains(role)) {
      return member;
    }
    Set<MemberRole> newRoles = new java.util.HashSet<>(member.roles());
    newRoles.add(role);

    // if a real role is added, remove NONE to keep the set meaningful
    if (role != MemberRole.NONE) {
      newRoles.remove(MemberRole.NONE);
    }

    Member updated = new Member(
        member.id(),
        member.email(),
        member.name(),
        member.phone(),
        member.passwordHash(),
        member.passwordSalt(),
        member.countryCode(),
        member.countryName(),
        member.nationalityCode(),
        member.nationalityName(),
        Set.copyOf(newRoles),
        member.createdAt()
    );
    Member saved = repository.save(updated);
    eventBus.publish(new MemberRoleAdded(saved.id(), role));
    return saved;
  }

  @Transactional
  public Member updateMyProfile(
      MemberId memberId,
      String name,
      UpdateMyMemberProfileRequest.CountryPayload country,
      UpdateMyMemberProfileRequest.NationalityPayload nationality
  ) {
    Objects.requireNonNull(memberId, "memberId");

    Member member = repository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));

    String countryCode = country == null ? null : country.code();
    String countryName = country == null ? null : country.name();
    String nationalityCode = nationality == null ? null : nationality.code();
    String nationalityName = nationality == null ? null : nationality.name();

    String trimmedName = name == null ? null : name.trim();
    if (trimmedName != null && trimmedName.isBlank()) {
      trimmedName = null;
    }

    Member updated = new Member(
        member.id(),
        member.email(),
        trimmedName,
        member.phone(),
        member.passwordHash(),
        member.passwordSalt(),
        countryCode,
        countryName,
        nationalityCode,
        nationalityName,
        member.roles(),
        member.createdAt()
    );
    return repository.save(updated);
  }

  @Override
  public Member login(String email, String password) {
    Objects.requireNonNull(email, "email");
    Objects.requireNonNull(password, "password");
    String normalizedEmail = email.trim().toLowerCase();

    Member member = repository.findByEmail(normalizedEmail).orElseThrow(InvalidCredentialsException::new);
    boolean ok = passwordHasher.verify(password.toCharArray(), member.passwordHash(), member.passwordSalt());
    if (!ok) {
      throw new InvalidCredentialsException();
    }
    return member;
  }

  @Override
  public Optional<Member> get(MemberId id) {
    Objects.requireNonNull(id, "id");
    return repository.findById(id);
  }
}

