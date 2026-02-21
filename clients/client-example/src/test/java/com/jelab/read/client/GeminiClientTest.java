package com.jelab.read.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.Models;
import com.google.genai.errors.ClientException;
import com.google.genai.errors.ServerException;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.jelab.read.client.dto.GeminiResponseDto;
import com.jelab.read.client.exception.GeminiQuotaExceededException;
import com.jelab.read.client.exception.GeminiServerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class GeminiClientTest {

    private final String VERSION = "gemini-1.5-flash";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private Client client;
    @Mock
    private Models models; // client.models 가 접근하는 실제 객체 모킹
    @Mock
    private GenerateContentResponse mockResponse;
    private GeminiClient geminiClient;

    @BeforeEach
    void setUp() throws Exception {
        var field = Client.class.getDeclaredField("models");
        field.setAccessible(true);
        field.set(client, models);

        // 2. 실제 objectMapper 주입
        geminiClient = new GeminiClient(client, VERSION, objectMapper);
    }


    @Test
    @DisplayName("성공: 제미니가 준 JSON 응답을 DTO로 변환하여 반환해야 한다")
    void analyzeImage_Success() throws Exception {
        String mockJson = """
                {
                  "status": "caution",
                  "summary": "계약서 요약입니다.",
                  "clauses": [
                    {
                      "location": "제1조",
                      "original": "원본",
                      "translation": "번역",
                      "tip": "팁",
                      "risk": "medium"
                    }
                  ]
                }
                """;

        when(mockResponse.text()).thenReturn(mockJson);
        when(models.generateContent(anyString(), any(Content.class), any())).thenReturn(mockResponse);

        // [When]
        MockMultipartFile file = new MockMultipartFile("img", "test.jpg", "image/jpeg", "data".getBytes());
        GeminiResponseDto result = geminiClient.analyzeImage(file);

        // [Then] 3. 실제 파싱된 결과값 검증
        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo("caution");
        assertThat(result.summary()).isEqualTo("계약서 요약입니다.");
        assertThat(result.clauses()).hasSize(1);
        assertThat(result.clauses().get(0).location()).isEqualTo("제1조");
        assertThat(result.clauses().get(0).risk()).isEqualTo("medium");
    }

    @Test
    @DisplayName("예외: 429 에러 발생 시 QuotaExceeded 예외를 던져야 한다.")
    void analyzeImage_QuotaError() {
        // [Given] 429 에러를 내뱉는 가짜 에러 생성
        ClientException quotaError = mock(ClientException.class);
        when(quotaError.code()).thenReturn(429);

        when(models.generateContent(anyString(), any(Content.class), any())).thenThrow(quotaError);

        // [When & Then] 해당 예외가 발생하는지 검증
        MockMultipartFile file = new MockMultipartFile("img", "test.jpg", "image/jpeg", "data".getBytes());
        assertThatThrownBy(() -> geminiClient.analyzeImage(file))
                .isInstanceOf(GeminiQuotaExceededException.class);
    }

    @Test
    @DisplayName("예외: 구글 서버 에러(500) 발생 시 GeminiServerException을 던져야 한다")
    void analyzeImage_ServerError() {
        // [Given] 500 에러 시뮬레이션

        ServerException geminiServerError = mock(ServerException.class);

        when(models.generateContent(anyString(), any(Content.class), any()))
                .thenThrow(geminiServerError);

        // [When & Then]
        MockMultipartFile file = new MockMultipartFile("img", "test.jpg", "image/jpeg", "data".getBytes());
        assertThatThrownBy(() -> geminiClient.analyzeImage(file))
                .isInstanceOf(GeminiServerException.class)
                .hasMessageContaining("Gemini Server Error");
    }
}