package com.jelab.read.client.exception;

public class GeminiClientException extends RuntimeException {

    public GeminiClientException(String message) {
        super(message);
    }

    public GeminiClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
