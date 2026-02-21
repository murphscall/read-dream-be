package com.jelab.read.core.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jelab.read.client.GeminiClient;
import com.jelab.read.client.dto.GeminiResponseDto;
import com.jelab.read.core.api.controller.v1.request.AnalyzeRequestDto;
import com.jelab.read.core.api.controller.v1.response.AnalyzeResponseDto;
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

        GeminiResponseDto geminiResponseDto = geminiClient.analyzeImage(analysisImage.getFile());

        return new AnalyzeResponseDto(
                geminiResponseDto.status(),
                geminiResponseDto.summary(),
                geminiResponseDto.clauses().stream()
                        .map(clause -> new AnalyzeResponseDto.Clause(
                                clause.location(),
                                clause.original(),
                                clause.translation(),
                                clause.tip(),
                                clause.risk()
                        ))
                        .toList()
        );
    }

}
