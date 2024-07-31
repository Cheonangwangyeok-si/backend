package com.likelion.mindiary.domain.dailyEmotion.repository;


import com.likelion.mindiary.domain.dailyEmotion.model.DailyEmotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyEmotionRepository extends JpaRepository<DailyEmotion, Long> {

    void deleteByDiary_DiaryId(Long diaryId);

}
