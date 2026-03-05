package com.jelab.read.core.domain;

import com.jelab.read.client.ai.GeminiClient;
import com.jelab.read.client.ai.exception.GeminiClientException;
import com.jelab.read.client.ai.exception.GeminiPermissionDeniedException;
import com.jelab.read.client.ai.exception.GeminiQuotaExceededException;
import com.jelab.read.client.ai.exception.GeminiServerException;
import com.jelab.read.client.ai.model.AiClientResult;
import com.jelab.read.core.api.controller.v1.request.AnalyzeRequestDto;
import com.jelab.read.core.api.controller.v1.response.AnalyzeResponseDto;
import com.jelab.read.core.support.error.CoreException;
import com.jelab.read.core.support.error.ErrorType;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeService {

    private final GeminiClient geminiClient;

    public AnalyzeService(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public AnalyzeResponseDto processAnalyze(AnalyzeRequestDto dto) {

        AnalysisImage analysisImage = new AnalysisImage(dto.getImageFile());

        try {
            AiClientResult result = geminiClient.analyzeImage(analysisImage.getFile());
            return new AnalyzeResponseDto(result.status(), result.summary(),
                    result.clauses()
                        .stream()
                        .map(clause -> new AnalyzeResponseDto.Clause(clause.location(), clause.original(),
                                clause.translation(), clause.tip(), clause.risk()))
                        .toList());
        }
        catch (GeminiQuotaExceededException e) {
            throw new CoreException(ErrorType.GEMINI_QUOTA_EXCEEDED, e.getMessage());
        }
        catch (GeminiPermissionDeniedException | GeminiClientException e) {
            throw new CoreException(ErrorType.GEMINI_CLIENT_ERROR, e.getMessage());
        }
        catch (GeminiServerException e) {
            throw new CoreException(ErrorType.GEMINI_SERVER_ERROR, e.getMessage());
        }

    }

}
