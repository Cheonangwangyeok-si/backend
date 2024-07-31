package com.likelion.mindiary.domain.weeklyEmotion.service;

import com.likelion.mindiary.domain.account.repository.AccountRepository;
import com.likelion.mindiary.domain.weeklyEmotion.controller.dto.response.WeeklyEmotionResponse;
import com.likelion.mindiary.domain.weeklyEmotion.exception.InvalidEndDateException;
import com.likelion.mindiary.domain.weeklyEmotion.model.WeeklyEmotion;
import com.likelion.mindiary.domain.weeklyEmotion.repository.WeeklyEmotionRepository;
import com.likelion.mindiary.global.Security.CustomUserDetails;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeeklyEmotionService {

    private final WeeklyEmotionRepository weeklyEmotionRepository;
    private final AccountRepository accountRepository;

    public WeeklyEmotionResponse getWeeklyEmotion(LocalDate endDate,
            CustomUserDetails userDetails) {

        if (!endDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            throw new InvalidEndDateException();
        }

        Long accountId = accountRepository.findByLoginId(userDetails.getProvidedId())
                .getAccountId();

        LocalDate startOfWeek = endDate.minusDays(6);  // 일요일 기준
        LocalDate prevEndOfWeek = startOfWeek.minusDays(1);
        LocalDate prevStartOfWeek = prevEndOfWeek.minusDays(6);

        List<WeeklyEmotion> currentWeekEmotionList =
                weeklyEmotionRepository.findByWeekStartDateAndWeekEndDateAndAccount_AccountId(
                        startOfWeek, endDate, accountId);

        WeeklyEmotion currentWeek =
                currentWeekEmotionList.isEmpty() ? null : currentWeekEmotionList.get(0);

        List<WeeklyEmotion> prevWeekEmotionList =
                weeklyEmotionRepository.findByWeekStartDateAndWeekEndDateAndAccount_AccountId(
                        prevStartOfWeek, prevEndOfWeek, accountId);

        WeeklyEmotion prevWeek = prevWeekEmotionList.isEmpty() ? null : prevWeekEmotionList.get(0);

        return new WeeklyEmotionResponse(
                currentWeek != null ? currentWeek.getAvgHappiness() : 0,
                currentWeek != null ? currentWeek.getAvgSadness() : 0,
                currentWeek != null ? currentWeek.getAvgAnger() : 0,
                currentWeek != null ? currentWeek.getAvgSurprise() : 0,
                currentWeek != null ? currentWeek.getAvgNeutral() : 0,
                currentWeek != null ? currentWeek.getWeeklyDetailedFeedback() : "",
                prevWeek != null ? prevWeek.getAvgHappiness() : 0,
                prevWeek != null ? prevWeek.getAvgSadness() : 0,
                prevWeek != null ? prevWeek.getAvgAnger() : 0,
                prevWeek != null ? prevWeek.getAvgSurprise() : 0,
                prevWeek != null ? prevWeek.getAvgNeutral() : 0
        );
    }
}
