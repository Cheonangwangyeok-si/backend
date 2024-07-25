package com.likelion.mindiary.domain.dairyEmotion.model;

import com.likelion.mindiary.domain.diary.model.Diary;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyEmotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JoinColumn(name = "diary_id")
    private Diary diary;
}
