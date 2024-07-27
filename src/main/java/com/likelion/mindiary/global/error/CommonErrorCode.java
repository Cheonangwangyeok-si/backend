package com.likelion.mindiary.global.error;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_INPUT_VALUE("C001", "올바르지 않은 입력 값입니다.", 400),
    METHOD_NOT_ALLOWED("C002", " 올바르지 않은 호출입니다.", 405),
    INTERNAL_SERVER_ERROR("C003", "서버 에러", 500),
    INVALID_TYPE_VALUE("C004", "바르지 않은 타입의 값을 입력했습니다.", 400),
    INVALID_REQUEST("C005", "잘못된 요청입니다", 400),

    // 인증 관련 에러
    UNAUTHENTICATED("A001", "인증이 필요합니다", 401),
    UNAUTHORIZED("A002", "권한이 없습니다", 403);

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