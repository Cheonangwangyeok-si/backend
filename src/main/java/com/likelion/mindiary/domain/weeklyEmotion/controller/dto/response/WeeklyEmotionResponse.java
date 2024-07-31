package com.likelion.mindiary.domain.weeklyEmotion.controller.dto.response;

public record WeeklyEmotionResponse(
        double currentAvgHappiness,
        double currentAvgSadness,
        double currentAvgAnger,
        double currentAvgSurprise,
        double currentAvgNeutral,
        String currentWeeklyDetailedFeedback,
        double prevAvgHappiness,
        double prevAvgSadness,
        double prevAvgAnger,
        double prevAvgSurprise,
        double prevAvgNeutral
) {}
