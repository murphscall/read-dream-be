package com.jelab.read.core.api.controller.v1;

import com.jelab.read.core.api.controller.v1.request.AnalyzeRequestDto;
import com.jelab.read.core.api.controller.v1.response.AnalyzeResponseDto;
import com.jelab.read.core.domain.AnalyzeService;
import com.jelab.read.core.support.response.ApiResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AnalyzeController {

    private final AnalyzeService analyzeService;

    public AnalyzeController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }

    @PostMapping("/analyze")
    public ApiResponse<AnalyzeResponseDto> analyzePost(@ModelAttribute AnalyzeRequestDto analyzeRequest) {
        AnalyzeResponseDto analyzeResult = analyzeService.processAnalyze(analyzeRequest);
        return ApiResponse.success(analyzeResult);
    }

}
