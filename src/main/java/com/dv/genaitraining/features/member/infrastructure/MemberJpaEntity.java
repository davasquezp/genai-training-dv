package com.dv.genaitraining.features.member.infrastructure;

import com.dv.genaitraining.features.member.domain.MemberRole;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * JPA entity for members.
 */
@Entity
@Table(name = "members")
public class MemberJpaEntity {
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "phone", nullable = false, unique = true)
  private String phone;

  @Column(name = "password_hash", nullable = false, length = 512)
  private String passwordHash;

  @Column(name = "password_salt", nullable = false, length = 512)
  private String passwordSalt;

  @Column(name = "country_code")
  private String countryCode;

  @Column(name = "country_name")
  private String countryName;

  @Column(name = "nationality_code")
  private String nationalityCode;

  @Column(name = "nationality_name")
  private String nationalityName;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private Set<MemberRole> roles = new HashSet<>();

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  protected MemberJpaEntity() {}

  public MemberJpaEntity(
      UUID id,
      String email,
      String name,
      String phone,
      String passwordHash,
      String passwordSalt,
      String countryCode,
      String countryName,
      String nationalityCode,
      String nationalityName,
      Set<MemberRole> roles,
      Instant createdAt
  ) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.phone = phone;
    this.passwordHash = passwordHash;
    this.passwordSalt = passwordSalt;
    this.countryCode = countryCode;
    this.countryName = countryName;
    this.nationalityCode = nationalityCode;
    this.nationalityName = nationalityName;
    this.roles = roles == null ? new HashSet<>() : new HashSet<>(roles);
    this.createdAt = createdAt;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getPasswordSalt() {
    return passwordSalt;
  }

  public void setPasswordSalt(String passwordSalt) {
    this.passwordSalt = passwordSalt;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public String getNationalityCode() {
    return nationalityCode;
  }

  public void setNationalityCode(String nationalityCode) {
    this.nationalityCode = nationalityCode;
  }

  public String getNationalityName() {
    return nationalityName;
  }

  public void setNationalityName(String nationalityName) {
    this.nationalityName = nationalityName;
  }

  public Set<MemberRole> getRoles() {
    return roles;
  }

  public void setRoles(Set<MemberRole> roles) {
    this.roles = roles;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}

