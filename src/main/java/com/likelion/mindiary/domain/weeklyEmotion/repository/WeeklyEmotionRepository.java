package com.likelion.mindiary.domain.weeklyEmotion.repository;

import com.likelion.mindiary.domain.weeklyEmotion.model.WeeklyEmotion;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyEmotionRepository extends JpaRepository<WeeklyEmotion, Long> {

    List<WeeklyEmotion> findByWeekStartDateAndWeekEndDateAndAccount_AccountId(
            LocalDate weekStartDate, LocalDate weekEndDate, Long accountId);

}
