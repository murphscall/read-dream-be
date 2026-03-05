package com.jelab.read.client.auth.client;

import com.jelab.read.client.auth.GoogleApi;
import com.jelab.read.client.auth.GoogleAuthApi;
import com.jelab.read.client.auth.dto.GoogleTokenResponse;
import com.jelab.read.client.auth.dto.OAuthUserResponse;
import com.jelab.read.core.enums.SocialType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GoogleOAuthClient implements OAuthClient {

    private final GoogleApi googleApi;

    private final GoogleAuthApi googleAuthApi;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.client.redirect-uri}")
    private String redirectUri;

    @Value("${google.client.grant_type}")
    private String grantType;

    public GoogleOAuthClient(final GoogleApi googleApi, final GoogleAuthApi googleAuthApi) {
        this.googleApi = googleApi;
        this.googleAuthApi = googleAuthApi;
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.GOOGLE;
    }

    @Override
    public String getLoginUrl() {
        return UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUri)
            .queryParam("response_type", "code")
            .queryParam("scope", "email profile")
            .build()
            .toUriString();
    }

    @Override
    public OAuthUserResponse getUserProfile(String code) {

        GoogleTokenResponse tokenResponse = googleAuthApi.getToken(clientId, clientSecret, code, grantType,
                redirectUri);

        String accessToken = "Bearer " + tokenResponse.getAccess_token();

        return googleApi.getUserInfo(accessToken);
    }

}
