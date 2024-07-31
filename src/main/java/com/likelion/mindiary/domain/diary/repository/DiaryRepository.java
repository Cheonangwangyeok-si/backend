package com.likelion.mindiary.domain.diary.repository;

import com.likelion.mindiary.domain.diary.model.Diary;
import com.likelion.mindiary.domain.weeklyEmotion.scheduler.dto.EmotionCommand;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("SELECT d, e.shortFeedback " +
            "FROM Diary d INNER JOIN DailyEmotion e ON d.diaryId = e.diary.diaryId " +
            "WHERE d.account.accountId = :accountId")
    List<Object[]> findDiaryAndShortFeedbackByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT d.diaryId, d.diaryAt, d.title, d.emotionType, e.shortFeedback " +
            "FROM Diary d INNER JOIN DailyEmotion e ON d.diaryId = e.diary.diaryId " +
            "WHERE d.account.accountId = :accountId " +
            "AND d.diaryAt BETWEEN :startDate AND :endDate")
    List<Object[]> findMonthDiaryByAccountId(
            @Param("accountId") Long accountId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT d.diaryAt "
            + "FROM Diary d "
            + "WHERE d.account.accountId = :accountId "
            + "AND d.diaryAt BETWEEN :startDate AND :endDate")
    List<LocalDate> findDiaryDatesByAccountIdAndDateRange(
            @Param("accountId") Long accountId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT d " +
            "FROM Diary d " +
            "WHERE d.account.accountId = :accountId " +
            "AND d.diaryAt BETWEEN :startDate AND :endDate")
    List<Diary> findDiariesForWeek(@Param("accountId") Long accountId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT new com.likelion.mindiary.domain.weeklyEmotion.scheduler.dto.EmotionCommand(de.happiness, de.sadness, de.anger, de.surprise, de.neutral) "
            + "FROM DailyEmotion de "
            + "WHERE de.diary.diaryId = :diaryId")
    EmotionCommand findEmotionByDiaryId(@Param("diaryId") Long diaryId);

}
