package com.likelion.mindiary.domain.dailyEmotion.repository;


import com.likelion.mindiary.domain.dailyEmotion.model.DailyEmotion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyEmotionRepository extends JpaRepository<DailyEmotion, Long> {

    void deleteByDiary_DiaryId(Long diaryId);

    Optional<DailyEmotion> findByDiary_DiaryId(Long diaryId);

    DailyEmotion findDailyEmotionByDiary_DiaryIdAndDiary_Account_AccountId(Long diaryId, Long accountId);
}
