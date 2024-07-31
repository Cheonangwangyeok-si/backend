package com.likelion.mindiary.domain.weeklyEmotion.exception;

import com.likelion.mindiary.global.error.exception.BusinessException;

public class InvalidEndDateException extends BusinessException {
    public InvalidEndDateException() {
        super(WeeklyEmotionErrorCode.INVALID_END_DATE);
    }
}
