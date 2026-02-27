package com.jelab.read.client.ai;

import com.jelab.read.client.ai.model.AiClientResult;
import java.util.List;

record GeminiResponseDto(
        String status,
        String summary,
        List<GeminiClause> clauses
) {
    /**
     * DTO를 서비스 전용 Result 객체로 변환합니다.
     */
    AiClientResult toResult() {
        List<AiClientResult.Clause> clauseResults = clauses.stream()
                .map(c -> new AiClientResult.Clause(
                        c.location(), c.original(), c.translation(), c.tip(), c.risk()
                ))
                .toList();

        return new AiClientResult(status, summary, clauseResults);
    }

    record GeminiClause(
            String location,
            String original,
            String translation,
            String tip,
            String risk
    ) {
    }
}
