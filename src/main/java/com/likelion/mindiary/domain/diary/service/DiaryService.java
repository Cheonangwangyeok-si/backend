package com.likelion.mindiary.domain.diary.service;

import com.likelion.mindiary.domain.account.model.Account;
import com.likelion.mindiary.domain.account.repository.AccountRepository;
import com.likelion.mindiary.domain.diary.controller.dto.request.AddDiaryRequest;
import com.likelion.mindiary.domain.diary.model.Diary;
import com.likelion.mindiary.domain.diary.repository.DiaryRepository;
import com.likelion.mindiary.global.Security.CustomOauth2UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final AccountRepository accountRepository;
    private final OpenAiClient openAiClient;

    public Diary addDiary(CustomOauth2UserDetails userDetails,
            AddDiaryRequest addDiaryRequest) {
        Account account = accountRepository.findByLoginId(userDetails.getProvidedId());
        Long accountId = account.getAccountId();

        Diary diary = Diary.builder()
                .title(addDiaryRequest.title())
                .content(addDiaryRequest.content())
                .diaryAt(addDiaryRequest.diaryAt())
                .emotionType(addDiaryRequest.emotionType())
                .account(Account.getAuthenticatedAccount(accountId))
                .build();

        Diary savedDiary = diaryRepository.save(diary);

        openAiClient.analyzeEmotion(addDiaryRequest.content(), savedDiary);

        return savedDiary;
    }
}
