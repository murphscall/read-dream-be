package com.jelab.read.core.support.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum ErrorType {

    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "An unexpected error has occurred.",
            LogLevel.ERROR),
    GEMINI_SERVER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, ErrorCode.E503_GEMINI_SERVER,
            "AI 서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.", LogLevel.ERROR),
    GEMINI_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500_GEMINI_CLIENT, "AI 서비스 처리 중 오류가 발생했습니다.",
            LogLevel.ERROR),
    GEMINI_QUOTA_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, ErrorCode.E429_GEMINI_QUOTA, "AI 요청 사용량이 많습니다. 잠시 후 다시 시도해주세요.",
            LogLevel.WARN),
    INVALID_IMAGE_FILE(HttpStatus.BAD_REQUEST, ErrorCode.E400_INVALID_IMAGE_FILE,
            "올바른 이미지 파일이 아닙니다. (지원 형식: jpeg, png, webp 등)", LogLevel.INFO),
    IMAGE_FILE_TOO_LARGE(HttpStatus.BAD_REQUEST, ErrorCode.E400_IMAGE_FILE_TOO_LARGE, "이미지 파일의 크기가 너무 큽니다. (20MB 이하)",
            LogLevel.INFO),
    GUEST_QUOTA_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, ErrorCode.E429_GUEST_QUOTA_EXCEEDED,
            "비회원 일일 이용 횟수(3회)를 모두 사용하셨습니다. 로그인 후 더 많은 혜택을 받아보세요!", LogLevel.INFO),
    JWT_EXPIRE(HttpStatus.UNAUTHORIZED, ErrorCode.E401_JWT_EXPIRED, "유효하지 않은 토큰입니다.", LogLevel.INFO),
    JWT_INVALID(HttpStatus.UNAUTHORIZED, ErrorCode.E401_JWT_INVALID, "유효하지 않은 토큰입니다.", LogLevel.WARN),
    JWT_UNSUPPORTED(HttpStatus.BAD_REQUEST, ErrorCode.E401_JWT_UNSUPPORTED, "잘못된 토큰입니다.", LogLevel.INFO),
    NOT_EXISTS_COOKIE(HttpStatus.BAD_REQUEST, ErrorCode.E400_NOT_EXISTS_COOKIE, "쿠키가 존재하지 않습니다", LogLevel.INFO),
    INVALID_NOT_FOUND_USER_TOKEN(HttpStatus.UNAUTHORIZED, ErrorCode.E401_NOT_FOUND_USER_TOKEN, "유효하지 않은 토큰입니다.",
            LogLevel.WARN);


    private final HttpStatus status;

    private final ErrorCode code;

    private final String message;

    private final LogLevel logLevel;

    ErrorType(HttpStatus status, ErrorCode code, String message, LogLevel logLevel) {

        this.status = status;
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ErrorCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

}
