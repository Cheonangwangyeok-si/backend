package com.likelion.mindiary.domain.weeklyEmotion.exception;

import com.likelion.mindiary.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WeeklyEmotionErrorCode implements ErrorCode {

    INVALID_END_DATE("W001", "요청한 날짜가 토요일이 아닙니다.", 400); // 새로운 에러 코드 추가

    private final String code;
    private final String message;
    private final int status;

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getStatus() {
        return this.status;
    }
}
