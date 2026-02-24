package com.jelab.read.core.support;

import jakarta.servlet.http.HttpServletRequest;

public class NetworkUtils {
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("CF-Connecting-IP");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return (ip != null && ip.contains(",")) ? ip.split(",")[0].trim() : ip;
    }
}
