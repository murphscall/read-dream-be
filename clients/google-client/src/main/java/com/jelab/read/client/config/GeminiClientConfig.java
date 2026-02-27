package com.jelab.read.client.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiClientConfig {

    @Bean
    public Client googleGenaiClient(@Value("${GOOGLE_API_KEY}") String apiKey) {

        return Client.builder().apiKey(apiKey).build();

    }

}
