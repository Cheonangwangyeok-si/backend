package com.likelion.mindiary.domain.diary.controller.dto.response;

import com.likelion.mindiary.domain.diary.model.Emotion;
import java.time.LocalDate;

public record GetDiaryResponse(
        Long diaryId,
        String title,
        String content,
        LocalDate diaryAt,
        Emotion emotionType,
        int happiness,
        int sadness,
        int anger,
        int surprise,
        int neutral,
        String detailedEmotion,
        String shortEmotion
) {

}
