package com.likelion.mindiary.domain.weeklyEmotion.repository;

import com.likelion.mindiary.domain.weeklyEmotion.model.WeeklyEmotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyEmotionRepository extends JpaRepository<WeeklyEmotion, Long> {

}
