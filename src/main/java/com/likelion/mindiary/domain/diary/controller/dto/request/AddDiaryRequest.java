package com.likelion.mindiary.domain.diary.controller.dto.request;

import com.likelion.mindiary.domain.diary.model.Emotion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Date;


public record AddDiaryRequest(
        @NotNull String title,
        @NotBlank String content,
        Date diaryAt,
        Emotion emotionType
) {

}
