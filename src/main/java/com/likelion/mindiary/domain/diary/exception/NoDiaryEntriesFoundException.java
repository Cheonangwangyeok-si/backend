package com.likelion.mindiary.domain.diary.exception;

import com.likelion.mindiary.global.error.exception.BusinessException;

public class NoDiaryEntriesFoundException extends BusinessException {

    public NoDiaryEntriesFoundException() {
        super(DiaryErrorCode.NO_DIARY_ENTRIES_FOUND);
    }
}
