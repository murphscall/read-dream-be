package com.jelab.read.core.enums;

public enum SocialType {

    GOOGLE, GITHUB, KAKAO, NAVER;

    public static SocialType from(String type) {
        return SocialType.valueOf(type.toUpperCase());
    }

}
