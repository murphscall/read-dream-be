package com.jelab.read.core.support;

import com.jelab.read.core.support.error.CoreException;
import com.jelab.read.core.support.error.ErrorType;
import com.jelab.read.redis.core.RedisUsageRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class UsageLimitInterceptor implements HandlerInterceptor {

    private final RedisUsageRepository redisUsageRepository;

    private final TokenProvider tokenProvider;

    public UsageLimitInterceptor(RedisUsageRepository redisUsageRepository, TokenProvider tokenProvider) {
        this.redisUsageRepository = redisUsageRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String accessToken = AuthorizationExtractor.extract(request);
        if (accessToken != null) {
            tokenProvider.validateToken(accessToken);
            return true;
        }

        String clientIp = NetworkUtils.getClientIp(request);

        long count = redisUsageRepository.incrementAndGet(clientIp);

        if (count > 3) {
            throw new CoreException(ErrorType.GUEST_QUOTA_EXCEEDED);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
