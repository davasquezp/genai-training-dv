package com.dv.genaitraining.features.dancer.application;

import com.dv.genaitraining.features.dancer.domain.DancerRepository;
import com.dv.genaitraining.features.member.application.MemberService;
import com.dv.genaitraining.features.member.domain.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberSignedUpFlowTest {
  @Autowired MemberService memberService;
  @Autowired DancerRepository dancerRepository;

  @Test
  void signup_doesNotCreateDancer_untilRoleAdded() {
    var member = memberService.signup("flow@example.com", "Flow User", "+1999999999", "secret");

    assertThat(dancerRepository.findByMemberId(member.id())).isEmpty();

    memberService.addRole(member.id(), MemberRole.DANCER);

    assertThat(dancerRepository.findByMemberId(member.id())).isPresent();
  }
}

