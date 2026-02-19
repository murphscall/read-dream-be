package com.jelab.read.core.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jelab.read.client.GeminiClient;
import com.jelab.read.core.api.controller.v1.request.AnalyzeRequestDto;
import com.jelab.read.core.api.controller.v1.response.AnalyzeResponseDto;
import com.jelab.read.core.support.error.exception.AiResponseException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeService {

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of("image/jpeg", "image/png", "image/webp",
            "image/heic", "image/heif");

    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024;

    private final GeminiClient geminiClient;

    private final ObjectMapper objectMapper;

    public AnalyzeService(GeminiClient geminiClient, ObjectMapper objectMapper) {
        this.geminiClient = geminiClient;
        this.objectMapper = objectMapper;
    }

    public AnalyzeResponseDto processAnalyze(AnalyzeRequestDto dto) {

        AnalysisImage analysisImage = new AnalysisImage(dto.getImageFile());

        String jsonResult = geminiClient.analyzeImage(analysisImage.getFile());

        try {
            return objectMapper.readValue(jsonResult, AnalyzeResponseDto.class);

        }
        catch (JsonProcessingException e) {
            throw new AiResponseException("AI 응답 DTO 파싱에 실패하였습니다.", e);
        }
    }

}
