package com.jelab.read.core.domain.dto;

public class LoginResponse {

    private final String accessToken;

    private final MemberInfo memberInfo;

    private LoginResponse(String accessToken, MemberInfo memberInfo) {
        this.accessToken = accessToken;
        this.memberInfo = memberInfo;
    }

    public static LoginResponse from(AuthResult authResult) {

        String accessToken = authResult.getAccessToken();

        return new LoginResponse(accessToken, authResult.getUserInfo());
    }

    public String getAccessToken() {
        return accessToken;
    }

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

}
