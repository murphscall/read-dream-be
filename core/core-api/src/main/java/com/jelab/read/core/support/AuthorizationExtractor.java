package com.jelab.read.core.support;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationExtractor {
    private static final String BEARER_TYPE = "Bearer ";

    public static String extract(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            return header.substring(BEARER_TYPE.length()).trim();
        }

        return null;
    }

}
