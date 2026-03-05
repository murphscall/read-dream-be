package com.jelab.read.core.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jelab.read.core.enums.SocialType;
import com.jelab.read.core.support.error.CoreException;
import com.jelab.read.core.support.error.ErrorType;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class TokenProviderTest {

    private final String secretKeyString = Encoders.BASE64
        .encode("test-secret-key-more-than-32-characters-long".getBytes());

    private final long validityInMilliseconds = 3600000; // 1시간

    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new TokenProvider(secretKeyString, validityInMilliseconds);
    }

    @Test
    @DisplayName("정상적인 토큰은 검증을 통과한다.")
    void validateToken_Success() {
        String accessToken = tokenProvider.createAccessToken(1L, "a@a.com", "name", SocialType.GOOGLE);

        assertThatCode(() -> tokenProvider.validateToken(accessToken)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("JWT 토큰이 만료 되었다면 예외가 발생한다.")
    void validateToken_Expired() {
        TokenProvider expiredProvider = new TokenProvider(secretKeyString, 0);
        String expiredToken = expiredProvider.createAccessToken(1L, "a@a.com", "name", SocialType.KAKAO);

        assertThatThrownBy(() -> tokenProvider.validateToken(expiredToken)).isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("errorType", ErrorType.JWT_EXPIRE);

    }

    @Test
    @DisplayName("잘못된 서명인 토큰은 JWT_INVALID 예외가 발생한다.")
    void validateToken_InvalidSignature() {
        String otherSecret = Encoders.BASE64.encode("different-secret-key-value-for-test-12345".getBytes());
        TokenProvider otherProvider = new TokenProvider(otherSecret, 3600000);
        String forgedToken = otherProvider.createAccessToken(1L, "a@a.com", "name", SocialType.KAKAO);

        assertThatThrownBy(() -> tokenProvider.validateToken(forgedToken)).isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("errorType", ErrorType.JWT_INVALID);

    }

    @Test
    @DisplayName("형식이 올바르지 않은 토큰은 JWT_INVALID 예외가 발생한다.")
    void validateToken_Malformed() {
        String malformedToken = "invalid.token.structure~~";
        assertThatThrownBy(() -> tokenProvider.validateToken(malformedToken)).isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("errorType", ErrorType.JWT_INVALID);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("토큰이 빈 문자열이거나 null이면 예외가 발생한다.")
    void validateToken_EmptyAndNull(String token) {
        assertThatThrownBy(() -> tokenProvider.validateToken(token)).isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("리프레쉬 토큰은 UUID 형식의 문자열을 반환한다.")
    void createRefreshToke() {
        String refreshToken = tokenProvider.createRefreshToken();

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.split("-")).hasSize(5);
    }

}