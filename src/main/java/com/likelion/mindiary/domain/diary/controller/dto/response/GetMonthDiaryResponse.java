package com.likelion.mindiary.domain.diary.controller.dto.response;

import com.likelion.mindiary.domain.diary.model.Emotion;
import java.time.LocalDate;

public record GetMonthDiaryResponse(
        Long diaryId,
        LocalDate diaryAt,
        String title,
        Emotion emotionType,
        String shortFeedback
) {

}

