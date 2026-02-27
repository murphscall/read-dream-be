package com.jelab.read.client.auth;

import com.jelab.read.client.auth.dto.GoogleUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleApi", url = "https://www.googleapis.com")
public interface GoogleApi {

    @GetMapping("/oauth2/v3/userinfo")
    GoogleUserResponse getUserInfo(@RequestHeader("Authorization") String accessToken);

}
