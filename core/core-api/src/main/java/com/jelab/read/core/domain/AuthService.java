package com.jelab.read.core.domain;

import com.jelab.read.client.auth.client.OAuthClient;
import com.jelab.read.client.auth.dto.OAuthUserResponse;
import com.jelab.read.core.domain.dto.AuthResult;
import com.jelab.read.core.domain.dto.AuthToken;
import com.jelab.read.core.domain.dto.MemberInfo;
import com.jelab.read.core.enums.SocialType;
import com.jelab.read.storage.db.core.member.Member;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final Map<SocialType, OAuthClient> clients;
    private final MemberService memberService;
    private final TokenService tokenService;

    public AuthService(List<OAuthClient> clientList, MemberService memberService, TokenService tokenService) {
        this.clients = clientList.stream().collect(Collectors.toMap(OAuthClient::getSocialType, client -> client));
        this.memberService = memberService;
        this.tokenService = tokenService;
    }


    public String getLoginUrl(String type) {

        OAuthClient client = clients.get(SocialType.from(type));

        return client.getLoginUrl();
    }

    public AuthResult socialLogin(String type, String code) {

        SocialType socialType = SocialType.from(type);
        OAuthClient client = clients.get(socialType);

        OAuthUserResponse socialUserInfo = client.getUserProfile(code);

        Member member = memberService.findOrCreateMember(
                socialUserInfo.getSocialId(),
                socialUserInfo.getEmail(),
                socialUserInfo.getName(),
                socialType
        );

        AuthToken authToken = tokenService.generateAuthToken(member.getSocialId(), member.getEmail(), member.getName(),
                member.getSocialType());

        return AuthResult.of(authToken, MemberInfo.toDto(member));

    }
}
