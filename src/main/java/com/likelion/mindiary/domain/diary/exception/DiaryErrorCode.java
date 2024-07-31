package com.likelion.mindiary.domain.diary.exception;

import com.likelion.mindiary.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DiaryErrorCode implements ErrorCode {

    EMOTION_ANALYSIS_FAILED("D001", "감정 분석에 실패했습니다.", 500),
    RESPONSE_PARSE_FAILED("D002", "응답 파싱에 실패했습니다.", 500),
    DIARY_Not_FOUND("D003", "일기 항목을 찾을 수 없습니다.", 404),
    NOT_DIARY_OWNER("D004", "권한이 없습니다.", 403);

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
