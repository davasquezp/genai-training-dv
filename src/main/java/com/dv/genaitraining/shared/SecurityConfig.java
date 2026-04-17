package com.dv.genaitraining.shared;

import com.dv.genaitraining.features.member.api.AuthController;
import com.dv.genaitraining.features.member.application.JwtService;
import com.dv.genaitraining.features.member.domain.MemberRole;
import com.dv.genaitraining.shared.security.TokenBlacklist;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Minimal JWT-based security configuration.
 */
@Configuration
public class SecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      AuthController authController,
      TokenBlacklist tokenBlacklist
  ) throws Exception {
    JwtService jwtService = authController.jwtService();

    http
        .csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, "/api/members/signup").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/members/me/roles").authenticated()
            .requestMatchers(HttpMethod.PUT, "/api/members/me/profile").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/dancers/me").authenticated()
            .requestMatchers(HttpMethod.PUT, "/api/dancers/me").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/community-memberships").hasRole("DANCER")
            .anyRequest().permitAll()
        )
        .addFilterBefore(new JwtAuthFilter(jwtService, tokenBlacklist), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  static class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final TokenBlacklist tokenBlacklist;

    JwtAuthFilter(JwtService jwtService, TokenBlacklist tokenBlacklist) {
      this.jwtService = jwtService;
      this.tokenBlacklist = tokenBlacklist;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
      String header = request.getHeader("Authorization");
      if (header != null && header.startsWith("Bearer ")) {
        String token = header.substring("Bearer ".length()).trim();
        try {
          JwtService.VerifiedToken vt = jwtService.verify(token);
          if (tokenBlacklist.isRevoked(vt.jti())) {
            filterChain.doFilter(request, response);
            return;
          }
          Set<SimpleGrantedAuthority> authorities = vt.roles().stream()
              .map(MemberRole::name)
              .map(r -> "ROLE_" + r)
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toSet());
          var auth = new UsernamePasswordAuthenticationToken(vt.memberId().toString(), token, authorities);
          org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception ignored) {
          // invalid token -> treat as unauthenticated
        }
      }
      filterChain.doFilter(request, response);
    }
  }
}

