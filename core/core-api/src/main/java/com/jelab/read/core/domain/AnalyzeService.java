package com.jelab.read.core.domain;

import com.jelab.read.client.GeminiClient;
import com.jelab.read.client.dto.GeminiResponseDto;
import com.jelab.read.core.api.controller.v1.request.AnalyzeRequestDto;
import com.jelab.read.core.api.controller.v1.response.AnalyzeResponseDto;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeService {

    private final GeminiClient geminiClient;

    public AnalyzeService(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
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
