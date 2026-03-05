package com.jelab.read.core.support.error.exception;

public class OAuthLoginException extends RuntimeException {

    public OAuthLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuthLoginException(String message) {
        super(message);
    }

}
