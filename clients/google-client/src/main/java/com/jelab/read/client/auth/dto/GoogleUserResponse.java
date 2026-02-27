package com.jelab.read.client.auth.dto;

public class GoogleUserResponse implements OAuthUserResponse {

    private final String sub;
    private final String email;
    private final String name;

    public GoogleUserResponse(String sub, String email, String name) {
        this.sub = sub;
        this.email = email;
        this.name = name;
    }

    @Override
    public String getSocialId() {
        return this.sub;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
