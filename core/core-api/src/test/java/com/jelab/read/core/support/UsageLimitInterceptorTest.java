package com.jelab.read.core.support;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.jelab.read.core.support.error.CoreException;
import com.jelab.read.core.support.error.ErrorType;
import com.jelab.read.redis.core.RedisUsageRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsageLimitInterceptorTest {

    @InjectMocks
    private UsageLimitInterceptor usageLimitInterceptor;

    @Mock
    private RedisUsageRepository redisUsageRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @Test
    @DisplayName("비회원은 하루 3회 이상 초과 요청 시 예외가 발생한다.")
    void guestExceedsDailyLimit() {

        String clientIp = "127.0.0.1";

        given(request.getHeader("CF-Connecting-IP")).willReturn(clientIp);

        given(redisUsageRepository.incrementAndGet(clientIp)).willReturn(4L);

        assertThatThrownBy(() -> usageLimitInterceptor.preHandle(request, response, handler))
            .isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("errorType", ErrorType.GUEST_QUOTA_EXCEEDED);

        verify(redisUsageRepository, times(1)).incrementAndGet(clientIp);
    }

}