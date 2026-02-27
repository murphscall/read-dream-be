package com.jelab.read.client.exception;

public class GeminiPermissionDeniedException extends RuntimeException {

    public GeminiPermissionDeniedException(String message) {
        super(message);
    }

    public GeminiPermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

}