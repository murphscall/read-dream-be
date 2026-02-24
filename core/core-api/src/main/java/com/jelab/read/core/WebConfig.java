package com.jelab.read.core;

import com.jelab.read.core.support.UsageLimitInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final UsageLimitInterceptor usageLimitInterceptor;

    public WebConfig(final UsageLimitInterceptor usageLimitInterceptor) {
        this.usageLimitInterceptor = usageLimitInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(usageLimitInterceptor)
                .addPathPatterns("/api/analyze")
                .excludePathPatterns("/static/**", "/favicon.ico");
    }
}
