package com.likelion.mindiary.domain.diary.service;

import com.likelion.mindiary.domain.account.model.Account;
import com.likelion.mindiary.domain.account.repository.AccountRepository;
import com.likelion.mindiary.domain.diary.controller.dto.request.AddDiaryRequest;
import com.likelion.mindiary.domain.diary.controller.dto.response.GetAllDiaryResponse;
import com.likelion.mindiary.domain.diary.controller.dto.response.GetMonthDiaryResponse;
import com.likelion.mindiary.domain.diary.exception.NoDiaryEntriesFoundException;
import com.likelion.mindiary.domain.diary.model.Diary;
import com.likelion.mindiary.domain.diary.model.Emotion;
import com.likelion.mindiary.domain.diary.repository.DiaryRepository;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.likelion.mindiary.global.Security.CustomUserDetails;
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

    public Diary addDiary(CustomUserDetails userDetails,
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

    public List<GetAllDiaryResponse> getAllDiaryWithFeedback(CustomUserDetails userDetails) {
        Account account = accountRepository.findByLoginId(userDetails.getProvidedId());
        Long accountId = account.getAccountId();

        List<Object[]> results = diaryRepository.findDiaryAndShortFeedbackByAccountId(accountId);

//        if (results.isEmpty()) {
//            throw new NoDiaryEntriesFoundException();
//        }

        return results.stream()
                .map(result -> {
                    Diary diary = (Diary) result[0];
                    String shortFeedback = (String) result[1];
                    return new GetAllDiaryResponse(
                            diary.getDiaryId(),
                            diary.getTitle(),
                            diary.getContent(),
                            diary.getDiaryAt().toInstant().atZone(java.time.ZoneId.systemDefault())
                                    .toLocalDate(),
                            diary.getEmotionType().getDescription(),
                            shortFeedback
                    );
                })
                .collect(Collectors.toList());
    }

    public List<GetMonthDiaryResponse> getMonthDiaryWithFeedback(
            CustomUserDetails userDetails,
            int year, int month) {
        Account account = accountRepository.findByLoginId(userDetails.getProvidedId());
        Long accountId = account.getAccountId();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();

        List<Object[]> results = diaryRepository.findMonthDiaryByAccountId(accountId, startDate,
                endDate);

        return results.stream()
                .map(result -> {
                    Date date = (Date) result[0];
                    String title = (String) result[1];
                    Emotion emotionType = (Emotion) result[2];
                    String shortEmotion = (String) result[3];
                    return new GetMonthDiaryResponse(
                            date,
                            title,
                            emotionType,
                            shortEmotion
                    );
                })
                .collect(Collectors.toList());

    }
}
