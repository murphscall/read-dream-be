package com.jelab.read.core.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jelab.read.core.enums.SocialType;
import com.jelab.read.storage.db.core.member.Member;
import com.jelab.read.storage.db.core.member.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private final String socialId = "google_12345";

    private final String email = "test@gmail.com";

    private final String name = "으니몬";

    private final SocialType type = SocialType.GOOGLE;

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이미 가입된 멤버가 있다면, 새로 저장하지 않고 기존 멤버를 반환한다")
    void findOrCreateMember_WhenExists() {
        Member existsMember = Member.createSocialMember(socialId, email, name, type);

        when(memberRepository.findBySocialIdAndSocialType(socialId, type)).thenReturn(Optional.of(existsMember));

        Member result = memberService.findOrCreateMember(socialId, email, name, type);

        assertThat(result).isEqualTo(existsMember);

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("가입된 멤버가 없다면, 새로운 멤버를 생성하고 저장한 뒤 반환한다")
    void findOrCreateMember_WhenNotExists() {
        when(memberRepository.findBySocialIdAndSocialType(socialId, type)).thenReturn(Optional.empty());

        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Member result = memberService.findOrCreateMember(socialId, email, name, type);

        assertThat(result.getSocialId()).isEqualTo(socialId);
        assertThat(result.getEmail()).isEqualTo(email);

        verify(memberRepository, times(1)).save(any(Member.class));
    }

}