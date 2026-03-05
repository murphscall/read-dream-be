package com.jelab.read.core.domain.dto;

public class AuthResult {

    private final AuthToken authToken;

    private final MemberInfo userInfo;

    private AuthResult(AuthToken authToken, MemberInfo userInfo) {
        this.authToken = authToken;
        this.userInfo = userInfo;

    }

    public static AuthResult of(AuthToken authToken, MemberInfo userInfo) {
        return new AuthResult(authToken, userInfo);
    }

    public String getAccessToken() {
        return authToken.getAccessToken();
    }

    public String getRefreshToken() {
        return authToken.getRefreshToken();
    }

    public MemberInfo getUserInfo() {
        return userInfo;
    }

}
