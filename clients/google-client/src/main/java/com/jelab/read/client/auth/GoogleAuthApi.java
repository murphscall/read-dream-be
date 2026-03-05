package com.jelab.read.client.auth;

import com.jelab.read.client.auth.dto.GoogleTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleAuthApi", url = "https://oauth2.googleapis.com")
public interface GoogleAuthApi {

    @PostMapping("/token")
    GoogleTokenResponse getToken(@RequestParam("client_id") String client_id,
            @RequestParam("client_secret") String client_secret, @RequestParam("code") String code,
            @RequestParam("grant_type") String grant_type, @RequestParam("redirect_uri") String redirect_uri);

}
