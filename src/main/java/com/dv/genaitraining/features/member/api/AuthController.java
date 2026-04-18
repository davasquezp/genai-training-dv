package com.dv.genaitraining.features.member.api;

import com.dv.genaitraining.features.member.application.JwtService;
import com.dv.genaitraining.features.member.application.LoginMemberUseCase;
import com.dv.genaitraining.features.member.domain.Member;
import com.dv.genaitraining.shared.security.TokenBlacklist;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter for authentication (write operations only).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final LoginMemberUseCase login;
  private final JwtService jwtService;
  private final TokenBlacklist tokenBlacklist;

  public AuthController(
      LoginMemberUseCase login,
      JwtService jwtService,
      TokenBlacklist tokenBlacklist
  ) {
    this.login = login;
    this.tokenBlacklist = tokenBlacklist;
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  public AuthResponse login(@Valid @RequestBody LoginRequest request) {
    Member member = login.login(request.email(), request.password());
    String token = jwtService.issue(member);
    return new AuthResponse(token, MemberController.toResponse(member));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(org.springframework.security.core.Authentication authentication) {
    if (authentication == null || authentication.getCredentials() == null) {
      return ResponseEntity.status(401).build();
    }
    String token = String.valueOf(authentication.getCredentials());
    JwtService.VerifiedToken vt = jwtService.verify(token);
    tokenBlacklist.revoke(vt.jti(), vt.expEpochSeconds());
    return ResponseEntity.noContent().build();
  }
}
