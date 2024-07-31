package com.likelion.mindiary.domain.dailyEmotion.model;

import com.likelion.mindiary.domain.diary.model.Diary;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DailyEmotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long dailyEmotionId;

    @Column(nullable = false)
    private int happiness;

    @Column(nullable = false)
    private int sadness;

    @Column(nullable = false)
    private int anger;

    @Column(nullable = false)
    private int surprise;

    @Column(nullable = false)
    private int neutral;

    @Column(nullable = false)
    private String detailedFeedback;

    @Column(nullable = false)
    private String shortFeedback;

    @OneToOne
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

}
