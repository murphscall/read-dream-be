package com.jelab.read.core.api.controller;

import com.jelab.read.client.exception.GeminiClientException;
import com.jelab.read.client.exception.GeminiQuotaExceededException;
import com.jelab.read.client.exception.GeminiServerException;
import com.jelab.read.core.support.error.CoreException;
import com.jelab.read.core.support.error.ErrorType;
import com.jelab.read.core.support.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ApiResponse<?>> handleCoreException(CoreException e) {
        switch (e.getErrorType().getLogLevel()) {
            case ERROR -> log.error("CoreException : {}", e.getMessage(), e);
            case WARN -> log.warn("CoreException : {}", e.getMessage(), e);
            default -> log.info("CoreException : {}", e.getMessage(), e);
        }
        return new ResponseEntity<>(ApiResponse.error(e.getErrorType(), e.getData()), e.getErrorType().getStatus());
    }

    @ExceptionHandler(GeminiClientException.class)
    public ResponseEntity<ApiResponse<?>> handleGeminiClientException(GeminiClientException e) {
        log.warn("GeminiClientException : {}", e.getMessage(), e);

        return new ResponseEntity<>(ApiResponse.error(ErrorType.GEMINI_CLIENT_ERROR),
                ErrorType.GEMINI_CLIENT_ERROR.getStatus());
    }

    @ExceptionHandler(GeminiServerException.class)
    public ResponseEntity<ApiResponse<?>> handleGeminiServerException(GeminiServerException e) {
        log.warn("GeminiServerException : {}", e.getMessage(), e);

        return new ResponseEntity<>(ApiResponse.error(ErrorType.GEMINI_SERVER_ERROR),
                ErrorType.GEMINI_SERVER_ERROR.getStatus());
    }

    @ExceptionHandler(GeminiQuotaExceededException.class)
    public ResponseEntity<ApiResponse<?>> handleGeminiQuotaExceededException(GeminiQuotaExceededException e) {
        log.warn("GeminiQuotaExceededException : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(ErrorType.GEMINI_QUOTA_EXCEEDED),
                ErrorType.GEMINI_QUOTA_EXCEEDED.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(ErrorType.DEFAULT_ERROR), ErrorType.DEFAULT_ERROR.getStatus());
    }

}
