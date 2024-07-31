package com.likelion.mindiary.domain.diary.exception;

import com.likelion.mindiary.global.error.exception.BusinessException;

public class NotDiaryOwnerException extends BusinessException {

    public NotDiaryOwnerException() {
        super(DiaryErrorCode.NOT_DIARY_OWNER);
    }
}
