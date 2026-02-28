package com.jelab.read.core.domain;

import com.jelab.read.core.domain.dto.AuthToken;
import com.jelab.read.core.enums.SocialType;
import com.jelab.read.core.support.TokenProvider;
import java.time.Duration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final StringRedisTemplate stringRedisTemplate;

    public TokenService(TokenProvider tokenProvider, StringRedisTemplate stringRedisTemplate) {
        this.tokenProvider = tokenProvider;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Transactional
    public AuthToken generateAuthToken(String socialId, String email, String name, SocialType socialType) {
        String accessToken = tokenProvider.createAccessToken(email, name, socialId, socialType);
        String refreshToken = generateAndSaveRefreshToken(socialId);

        return AuthToken.of(accessToken, refreshToken);
    }

    private String generateAndSaveRefreshToken(String socialId) {
        String refreshToken = tokenProvider.createRefreshToken();

        stringRedisTemplate.opsForValue().set("RT:" + socialId, refreshToken, Duration.ofDays(7));

        return refreshToken;
    }
}
