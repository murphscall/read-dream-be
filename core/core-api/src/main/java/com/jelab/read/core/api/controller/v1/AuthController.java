package com.jelab.read.core.api.controller.v1;

import com.jelab.read.core.domain.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/{socialType}/login")
    public void redirectLogin(@PathVariable("socialType") String socialType, HttpServletResponse response)
            throws IOException {

        String loginUrl = authService.getLoginUrl(socialType);

        response.sendRedirect(loginUrl);

    }


    @GetMapping("/{socialType}/callback")
    public void redirectCallback(@PathVariable("socialType") String socialType, @RequestParam("code") String code) {

        authService.socialLogin(socialType, code);

    }

}
