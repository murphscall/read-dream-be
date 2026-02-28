package com.jelab.read.core.api.controller.v1;

import com.jelab.read.core.domain.AuthService;
import com.jelab.read.core.domain.dto.AuthResult;
import com.jelab.read.core.domain.dto.LoginResponse;
import com.jelab.read.core.support.CookieManager;
import com.jelab.read.core.support.error.exception.OAuthLoginException;
import com.jelab.read.core.support.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CookieManager cookieManager;

    public AuthController(AuthService authService, CookieManager cookieManager) {
        this.authService = authService;
        this.cookieManager = cookieManager;
    }

    @GetMapping("/{socialType}/login")
    public void redirectLogin(@PathVariable("socialType") String socialType, HttpServletResponse response)
            throws IOException {

        String loginUrl = authService.getLoginUrl(socialType);

        response.sendRedirect(loginUrl);

    }


    @GetMapping("/{socialType}/callback")
    public ResponseEntity<ApiResponse<?>> redirectCallback(@PathVariable("socialType") String socialType,
                                                           @RequestParam(required = false) String code,
                                                           @RequestParam(required = false) String error,
                                                           HttpServletResponse response) {
        if (error != null) {
            throw new OAuthLoginException(error);
        }

        AuthResult authResult = authService.socialLogin(socialType, code);

        String refreshToken = authResult.getRefreshToken();
        ResponseCookie cookie = cookieManager.createRefreshTokenCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(ApiResponse.success(LoginResponse.from(authResult)));
    }

}
