package com.likelion.mindiary.domain.weeklyEmotion.scheduler;

import com.likelion.mindiary.domain.account.model.Account;
import com.likelion.mindiary.domain.account.repository.AccountRepository;
import com.likelion.mindiary.domain.diary.model.Diary;
import com.likelion.mindiary.domain.diary.repository.DiaryRepository;
import com.likelion.mindiary.domain.diary.service.OpenAiClient;
import com.likelion.mindiary.domain.weeklyEmotion.model.WeeklyEmotion;
import com.likelion.mindiary.domain.weeklyEmotion.repository.WeeklyEmotionRepository;
import com.likelion.mindiary.domain.weeklyEmotion.scheduler.dto.EmotionCommand;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeeklyEmotionScheduler {

    private final OpenAiClient openAiClient;
    private final DiaryRepository diaryRepository;
    private final AccountRepository accountRepository;
    private final WeeklyEmotionRepository weeklyEmotionRepository;

    @Scheduled(cron = "0 0 0 * * SUN")
    public void analyzeWeeklyEmotionForAllUsers() {
        List<Account> allAccounts = accountRepository.findAll();
        for (Account account : allAccounts) {
            analyzeWeeklyEmotionForUser(account.getAccountId());
        }
    }

    public void analyzeWeeklyEmotionForUser(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY);
        LocalDate endOfWeek = LocalDate.now().with(DayOfWeek.SATURDAY);

        LocalDate today = LocalDate.now();
        if (startOfWeek.isAfter(today)) {
            startOfWeek = startOfWeek.minusWeeks(1);
        }

        List<Diary> weeklyDiaries = diaryRepository.findDiariesForWeek(accountId, startOfWeek,
                endOfWeek);

        if (!weeklyDiaries.isEmpty()) {
            double totalHappiness = 0, totalSadness = 0, totalAnger = 0, totalSurprise = 0, totalNeutral = 0;
            int diaryCount = weeklyDiaries.size();

            for (Diary diary : weeklyDiaries) {
                EmotionCommand emotionCommand = diaryRepository.findEmotionByDiaryId(
                        diary.getDiaryId());
                if (emotionCommand != null) {
                    totalHappiness += emotionCommand.getHappiness();
                    totalSadness += emotionCommand.getSadness();
                    totalAnger += emotionCommand.getAnger();
                    totalSurprise += emotionCommand.getSurprise();
                    totalNeutral += emotionCommand.getNeutral();
                }
            }

            double avgHappiness = Math.round((totalHappiness / diaryCount) * 10) / 10.0;
            double avgSadness = Math.round((totalSadness / diaryCount) * 10) / 10.0;
            double avgAnger = Math.round((totalAnger / diaryCount) * 10) / 10.0;
            double avgSurprise = Math.round((totalSurprise / diaryCount) * 10) / 10.0;
            double avgNeutral = Math.round((totalNeutral / diaryCount) * 10) / 10.0;

            String weeklyContent = weeklyDiaries.stream()
                    .map(Diary::getContent)
                    .collect(Collectors.joining(" "));

            // 감정 결과를 포함한 피드백 요청 내용 생성
            String feedbackRequestContent = String.format(
                    "일주일간의 일기 내용:\n%s\n\n" +
                            "평균 감정 점수:\n" +
                            "행복: %.1f, 슬픔: %.1f, 분노: %.1f, 놀람: %.1f, 중립: %.1f",
                    weeklyContent, avgHappiness, avgSadness, avgAnger, avgSurprise, avgNeutral
            );

            String weeklyFeedback = openAiClient.generateWeeklyFeedback(feedbackRequestContent);

            WeeklyEmotion weeklyEmotion = WeeklyEmotion.builder()
                    .avgHappiness(avgHappiness)
                    .avgSadness(avgSadness)
                    .avgAnger(avgAnger)
                    .avgSurprise(avgSurprise)
                    .avgNeutral(avgNeutral)
                    .weeklyDetailedFeedback(weeklyFeedback)
                    .weekStartDate(startOfWeek)
                    .weekEndDate(endOfWeek)
                    .account(account)
                    .build();

            weeklyEmotionRepository.save(weeklyEmotion);
        }
    }
}
