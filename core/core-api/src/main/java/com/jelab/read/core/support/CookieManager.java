package com.jelab.read.core.support;

import com.jelab.read.core.support.error.CoreException;
import com.jelab.read.core.support.error.ErrorType;
import jakarta.servlet.http.Cookie;
import java.time.Duration;
import java.util.Objects;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {

    private static final String REFRESH_TOKEN = "refreshToken";
    private final Duration duration = Duration.ofDays(7);

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(duration)
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie createEmptyCookie() {
        return ResponseCookie.from(REFRESH_TOKEN, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
    }

    public void validateRefreshTokenCookie(Cookie cookie) {
        if (Objects.isNull(cookie)) {
            throw new CoreException(ErrorType.NOT_EXISTS_COOKIE);
        }
    }

}
