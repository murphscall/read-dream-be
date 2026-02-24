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

    public UsageLimitInterceptor(RedisUsageRepository redisUsageRepository) {
        this.redisUsageRepository = redisUsageRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientIp = NetworkUtils.getClientIp(request);

        System.out.println("요청 잘옴: " + clientIp);

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
