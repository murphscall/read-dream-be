package com.jelab.read.core.domain;

import com.jelab.read.core.enums.SocialType;
import com.jelab.read.storage.db.core.member.Member;
import com.jelab.read.storage.db.core.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member findOrCreateMember(String socialId, String email, String name, SocialType type) {
        return memberRepository.findBySocialIdAndSocialType(socialId, type).orElseGet(() -> {
            Member newMember = Member.createSocialMember(socialId, email, name, type);
            return addMember(newMember);
        });
    }

    private Member addMember(Member member) {
        return memberRepository.save(member);
    }

    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

}
