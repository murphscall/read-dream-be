package com.jelab.read.core.domain.dto;

import com.jelab.read.core.enums.SocialType;
import com.jelab.read.storage.db.core.member.Member;

public class MemberInfo {
    private final String socialId;
    private final String email;
    private final String name;
    private final SocialType socialType;

    private MemberInfo(String socialId, String email, String name, SocialType socialType) {
        this.socialId = socialId;
        this.email = email;
        this.name = name;
        this.socialType = socialType;
    }

    public static MemberInfo of(String socialId, String email, String name, SocialType socialType) {
        return new MemberInfo(socialId, email, name, socialType);
    }

    public static MemberInfo toDto(Member member) {
        return new MemberInfo(member.getSocialId(), member.getEmail(), member.getName(), member.getSocialType());
    }

    public String getSocialId() {
        return socialId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public SocialType getSocialType() {
        return socialType;
    }
}
