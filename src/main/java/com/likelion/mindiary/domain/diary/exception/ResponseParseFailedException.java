package com.likelion.mindiary.domain.diary.exception;

import com.likelion.mindiary.global.error.exception.BusinessException;

public class ResponseParseFailedException extends BusinessException {

    public ResponseParseFailedException() {
        super(DiaryErrorCode.RESPONSE_PARSE_FAILED);
    }
}
