package com.likelion.mindiary.domain.diary.repository;

import com.likelion.mindiary.domain.diary.model.Diary;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("SELECT d, e.shortFeedback " +
            "FROM Diary d INNER JOIN DailyEmotion e ON d.diaryId = e.diary.diaryId " +
            "WHERE d.account.accountId = :accountId")
    List<Object[]> findDiaryAndShortFeedbackByAccountId(@Param("accountId") Long accountId);
}
