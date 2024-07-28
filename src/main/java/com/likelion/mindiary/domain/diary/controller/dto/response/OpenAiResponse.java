package com.likelion.mindiary.domain.diary.controller.dto.response;

public record OpenAiResponse(
        String shortFeedback,
        String detailedFeedback,
        int happiness,
        int sadness,
        int anger,
        int surprise,
        int neutral
) {

}
