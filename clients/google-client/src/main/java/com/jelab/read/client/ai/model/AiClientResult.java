package com.jelab.read.client.ai.model;

import java.util.List;

public record AiClientResult(String status, // "safe", "caution", "danger"
        String summary, List<Clause> clauses) {
    public record Clause(String location, String original, String translation, String tip, String risk // "high",
                                                                                                       // "medium",
                                                                                                       // "low"
    ) {
    }
}
