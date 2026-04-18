package com.dv.genaitraining.features.member.api;

import com.dv.genaitraining.features.member.application.JwtService;
import com.dv.genaitraining.features.member.application.MemberService;
import com.dv.genaitraining.features.member.application.SignupMemberUseCase;
import com.dv.genaitraining.features.member.domain.Member;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dv.genaitraining.shared.ids.MemberId;

import java.net.URI;

/**
 * REST adapter for member signup.
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {
  private final SignupMemberUseCase signup;
  private final MemberService memberService;
  private final JwtService jwtService;

  public MemberController(SignupMemberUseCase signup, MemberService memberService, JwtService jwtService) {
    this.signup = signup;
    this.memberService = memberService;
    this.jwtService = jwtService;
  }

  @PostMapping("/signup")
  public ResponseEntity<MemberResponse> signup(@Valid @RequestBody SignupRequest request) {
    Member created = signup.signup(request.email(), request.name(), request.phone(), request.password());
    return ResponseEntity.created(URI.create("/api/members/" + created.id().value()))
        .body(toResponse(created));
  }

  @PostMapping("/me/roles")
  public ResponseEntity<AuthResponse> addRole(Authentication authentication, @Valid @RequestBody AddRoleRequest request) {
    if (authentication == null || authentication.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    java.util.UUID memberId = java.util.UUID.fromString(String.valueOf(authentication.getPrincipal()));
    Member updated = memberService.addRole(new MemberId(memberId), request.role());
    String token = jwtService.issue(updated);
    return ResponseEntity.ok(new AuthResponse(token, toResponse(updated)));
  }

  @PutMapping("/me/profile")
  public ResponseEntity<MemberResponse> updateMyProfile(
      Authentication authentication,
      @Valid @RequestBody UpdateMyMemberProfileRequest request
  ) {
    if (authentication == null || authentication.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    java.util.UUID memberId = java.util.UUID.fromString(String.valueOf(authentication.getPrincipal()));
    Member updated = memberService.updateMyProfile(
        new MemberId(memberId),
        request.name(),
        request.country(),
        request.nationality()
    );
    return ResponseEntity.ok(toResponse(updated));
  }

  static MemberResponse toResponse(Member m) {
    return new MemberResponse(
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
}

