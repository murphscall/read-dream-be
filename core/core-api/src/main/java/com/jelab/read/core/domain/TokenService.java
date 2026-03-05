package com.jelab.read.core.domain;

import com.jelab.read.core.domain.dto.AuthToken;
import com.jelab.read.core.enums.SocialType;
import com.jelab.read.core.support.TokenProvider;
import com.jelab.read.core.support.error.CoreException;
import com.jelab.read.core.support.error.ErrorType;
import java.time.Duration;
import java.util.Optional;
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
    public AuthToken generateAuthToken(Long memberId, String email, String name, SocialType socialType) {
        String accessToken = tokenProvider.createAccessToken(memberId, email, name, socialType);
        String refreshToken = generateAndSaveRefreshToken(memberId);

        return AuthToken.of(accessToken, refreshToken);
    }

    private String generateAndSaveRefreshToken(Long memberId) {
        String refreshToken = tokenProvider.createRefreshToken();

        stringRedisTemplate.opsForValue().set("RT:" + refreshToken, String.valueOf(memberId), Duration.ofDays(7));

        return refreshToken;
    }

    public String findSocialIdByRefreshToken(String refreshToken) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get("RT:" + refreshToken))
            .orElseThrow(() -> new CoreException(ErrorType.INVALID_NOT_FOUND_USER_TOKEN));
    }

    @Transactional
    public AuthToken generateNewAuthTokenAfterVerifyRefreshToken(String refreshToken, Long memberId, String email,
            String name, SocialType socialType) {

        stringRedisTemplate.delete("RT:" + refreshToken);
        return generateAuthToken(memberId, email, name, socialType);

    }

}
