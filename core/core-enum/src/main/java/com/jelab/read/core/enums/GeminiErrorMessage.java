package com.jelab.read.core.enums;

public enum GeminiErrorMessage {

    QUOTA_EXCEEDED("Gemini API 사용 할당량을 초과했습니다.:"), API_KEY_FAILED("Gemini API 인증 또는 권한에 문제가 있습니다. API 키를 확인해주세요.:"),
    BAD_REQUEST("Gemini API에 잘못된 요청을 보냈습니다:"), SERVER_ERROR("Gemini API 서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.:"),
    NETWORK_ERROR("Gemini API 와의 네트워크 통신 중 오류가 발생했습니다.:");

    private final String message;

    GeminiErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
