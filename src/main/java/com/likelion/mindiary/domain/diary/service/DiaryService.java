package com.likelion.mindiary.domain.diary.service;

import com.likelion.mindiary.domain.account.model.Account;
import com.likelion.mindiary.domain.account.repository.AccountRepository;
import com.likelion.mindiary.domain.diary.controller.dto.request.AddDiaryRequest;
import com.likelion.mindiary.domain.diary.controller.dto.response.GetAllDiaryResponse;
import com.likelion.mindiary.domain.diary.controller.dto.response.GetMonthDiaryResponse;
import com.likelion.mindiary.domain.diary.model.Diary;
import com.likelion.mindiary.domain.diary.model.Emotion;
import com.likelion.mindiary.domain.diary.repository.DiaryRepository;
import com.likelion.mindiary.global.Security.CustomUserDetails;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
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

    public Diary addDiary(CustomUserDetails userDetails, AddDiaryRequest addDiaryRequest) {
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
                            diary.getDiaryAt(),
                            diary.getEmotionType().getDescription(),
                            shortFeedback
                    );
                })
                .collect(Collectors.toList());
    }

    public List<GetMonthDiaryResponse> getMonthDiaryWithFeedback(CustomUserDetails userDetails,
            int year, int month) {
        Account account = accountRepository.findByLoginId(userDetails.getProvidedId());
        Long accountId = account.getAccountId();

        LocalDate startDate = LocalDate.of(year, month, 1); // 해당 월의 첫날
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth(); // 해당 월의 마지막 날

        List<Object[]> results = diaryRepository.findMonthDiaryByAccountId(accountId, startDate,
                endDate);

        return results.stream()
                .map(result -> {
                    LocalDate date = (LocalDate) result[0];
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
