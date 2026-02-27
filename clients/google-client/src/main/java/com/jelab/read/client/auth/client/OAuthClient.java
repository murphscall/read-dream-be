package com.jelab.read.client.auth.client;

import com.jelab.read.client.auth.dto.OAuthUserResponse;
import com.jelab.read.core.enums.SocialType;

public interface OAuthClient {

    SocialType getSocialType();

    String getLoginUrl();

    OAuthUserResponse getUserProfile(String code);
}
