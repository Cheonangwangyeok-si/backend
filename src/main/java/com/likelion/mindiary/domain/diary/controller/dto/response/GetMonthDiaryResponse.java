package com.likelion.mindiary.domain.diary.controller.dto.response;

import com.likelion.mindiary.domain.diary.model.Emotion;
import java.util.Date;

public record GetMonthDiaryResponse(
         Date diaryAt,
        String title,
        Emotion emotionType,
        String shortFeedback
) {

}

