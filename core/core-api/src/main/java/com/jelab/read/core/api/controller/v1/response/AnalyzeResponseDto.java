package com.jelab.read.core.api.controller.v1.response;

import java.util.List;

public record AnalyzeResponseDto(String status, String summary, List<Clause> clauses) {

    public record Clause(String location, String original, String translation, String tip, String risk) {
    }
}
