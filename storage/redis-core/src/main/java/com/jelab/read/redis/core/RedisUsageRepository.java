package com.jelab.read.redis.core;

import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUsageRepository {

    private static final String KEY_PREFIX = "usage:limit:guest:";

    private final StringRedisTemplate redisTemplate;

    public RedisUsageRepository(final StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public long incrementAndGet(String ip) {
        String key = KEY_PREFIX + ip;
        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
            Duration secondsUntilMidnight = Duration.between(now, midnight);

            redisTemplate.expire(key, secondsUntilMidnight);
        }

        return count != null ? count : 0;
    }

}
