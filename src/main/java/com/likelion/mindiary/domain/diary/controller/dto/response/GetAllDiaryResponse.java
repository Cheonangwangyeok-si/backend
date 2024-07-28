package com.likelion.mindiary.domain.diary.controller.dto.response;

import java.time.LocalDate;

public record GetAllDiaryResponse(
        Long diaryId,
        String title,
        String content,
        LocalDate diaryAt,
        String emotionType,
        String shortFeedback
) {

}
