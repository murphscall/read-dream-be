package com.jelab.read.client.exception;

public class GeminiServerException extends RuntimeException {

    public GeminiServerException(String message) {
        super(message);
    }

    public GeminiServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
