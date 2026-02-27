package com.jelab.read.client.utils;

import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import java.util.List;
import java.util.Map;

public class GeminiSchema {

    public static final Schema ANALYSIS_SCHEMA = Schema.builder()
        .type(new Type(Type.Known.OBJECT))
        .required(List.of("status", "summary", "clauses"))
        .properties(Map.of("status",
                Schema.builder()
                    .type(new Type(Type.Known.STRING))
                    .enum_(List.of("safe", "caution", "danger")) // enum_ 확인
                    .build(),
                "summary", Schema.builder().type(new Type(Type.Known.STRING)).build(), "clauses",
                Schema.builder()
                    .type(new Type(Type.Known.ARRAY))
                    .items(Schema.builder()
                        .type(new Type(Type.Known.OBJECT))
                        .required(List.of("location", "original", "translation", "tip", "risk"))
                        .properties(Map.of("location", Schema.builder().type(new Type(Type.Known.STRING)).build(),
                                "original", Schema.builder().type(new Type(Type.Known.STRING)).build(), "translation",
                                Schema.builder().type(new Type(Type.Known.STRING)).build(), "tip",
                                Schema.builder().type(new Type(Type.Known.STRING)).build(), "risk",
                                Schema.builder()
                                    .type(new Type(Type.Known.STRING))
                                    .enum_(List.of("high", "medium", "low"))
                                    .build()))
                        .build())
                    .build()))
        .build();

}
