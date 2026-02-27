package com.jelab.read.core.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.jelab.read.client.ai.GeminiClient;
import com.jelab.read.client.ai.exception.GeminiQuotaExceededException;
import com.jelab.read.core.api.controller.v1.request.AnalyzeRequestDto;
import com.jelab.read.core.support.error.CoreException;
import com.jelab.read.core.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class AnalyzeServiceTest {

    @Mock
    private GeminiClient geminiClient;

    @InjectMocks
    private AnalyzeService analyzeService;

    @Test
    @DisplayName("core exception으로 변환하여 반환한다.")
    void coreException_test() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg",
                "hello".getBytes());
        AnalyzeRequestDto analyzeRequestDto = new AnalyzeRequestDto();
        analyzeRequestDto.setImageFile(mockMultipartFile);

        when(geminiClient.analyzeImage(any())).thenThrow(GeminiQuotaExceededException.class);
        assertThatThrownBy(() -> analyzeService.processAnalyze(analyzeRequestDto)).isInstanceOf(CoreException.class)
                .hasMessage(ErrorType.GEMINI_QUOTA_EXCEEDED.getMessage());


    }
}