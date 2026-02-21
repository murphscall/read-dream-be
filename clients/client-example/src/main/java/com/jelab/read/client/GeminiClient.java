package com.jelab.read.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.errors.ClientException;
import com.google.genai.errors.ServerException;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.jelab.read.client.dto.GeminiResponseDto;
import com.jelab.read.client.exception.GeminiClientException;
import com.jelab.read.client.exception.GeminiQuotaExceededException;
import com.jelab.read.client.exception.GeminiServerException;
import com.jelab.read.client.utils.GeminiSchema;
import com.jelab.read.core.enums.Prompt;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class GeminiClient {

    private final String geminiVersion;

    private final Client client;

    private final ObjectMapper objectMapper;

    public GeminiClient(Client client, @Value("${gemini.version}") String geminiVersion, ObjectMapper objectMapper) {
        this.client = client;
        this.geminiVersion = geminiVersion;
        this.objectMapper = objectMapper;
    }

    public GeminiResponseDto analyzeImage(MultipartFile imageFile) {

        try {

            Part imagePart = Part.fromBytes(imageFile.getBytes(), imageFile.getContentType());

            // 질문을 텍스트 파트로 변환
            Part textPart = Part.fromText(Prompt.ANALYSIS_REQUEST.getContent());

            Content content = Content.builder().role("user").parts(List.of(textPart, imagePart)).build();

            GenerateContentResponse contentResponse = client.models.generateContent(geminiVersion, content,
                    createAnalysisConfig()

            );
            return objectMapper.readValue(contentResponse.text(), GeminiResponseDto.class);
        } catch (ClientException e) {
            throw geminiErrorsConverter(e);
        } catch (ServerException e) {
            throw new GeminiServerException("Gemini Server Error :" + e.getMessage(), e);
        } catch (IOException e) {
            throw new GeminiClientException("Gemini Network Error :" + e.getMessage(), e);
        }

    }

    private GenerateContentConfig createAnalysisConfig() {
        return GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .responseSchema(GeminiSchema.ANALYSIS_SCHEMA) // 스키마 클래스 활용
                .candidateCount(1)
                .build();
    }

    private RuntimeException geminiErrorsConverter(ClientException e) {
        return switch (e.code()) {
            case 429 -> new GeminiQuotaExceededException("Quota exceeded: " + e.getMessage(), e);
            case 401, 403 -> new GeminiClientException("API Key or Permission Error: " + e.getMessage(), e);
            default -> new GeminiClientException("Bad Request: " + e.getMessage(), e);
        };
    }

}
