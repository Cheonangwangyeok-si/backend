package com.likelion.mindiary.domain.diary.exception;

import com.likelion.mindiary.global.error.exception.BusinessException;

public class DiaryNotFoundException extends BusinessException {

    public DiaryNotFoundException() {
        super(DiaryErrorCode.DIARY_Not_FOUND);
    }
}
