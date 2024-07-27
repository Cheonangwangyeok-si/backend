package com.likelion.mindiary.domain.diary.exception;

import com.likelion.mindiary.global.error.exception.BusinessException;

public class EmotionAnalysisFailedException extends BusinessException {

    public EmotionAnalysisFailedException() {
        super(DiaryErrorCode.EMOTION_ANALYSIS_FAILED);
    }
}
