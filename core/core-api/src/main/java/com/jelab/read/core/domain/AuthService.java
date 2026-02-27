package com.jelab.read.core.domain;

import com.jelab.read.client.auth.client.OAuthClient;
import com.jelab.read.client.auth.dto.OAuthUserResponse;
import com.jelab.read.core.enums.SocialType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final Map<SocialType, OAuthClient> clients;

    public AuthService(List<OAuthClient> clientList) {
        this.clients = clientList.stream().collect(Collectors.toMap(OAuthClient::getSocialType, client -> client));
    }


    public String getLoginUrl(String type) {

        OAuthClient client = clients.get(SocialType.from(type));

        return client.getLoginUrl();
    }

    public void socialLogin(String type, String code) {

        SocialType socialType = SocialType.from(type);
        OAuthClient client = clients.get(socialType);

        OAuthUserResponse response = client.getUserProfile(code);
    }
}
