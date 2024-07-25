package com.likelion.mindiary.domain.weeklyEmotion.model;

import com.likelion.mindiary.domain.account.model.Account;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyEmotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekly_emotion_id")
    private Long weeklyEmotionId;

    @Column(nullable = false)
    private double avgHappiness;

    @Column(nullable = false)
    private double avgSadness;

    @Column(nullable = false)
    private double avgAnger;

    @Column(nullable = false)
    private double avgSurprise;

    @Column(nullable = false)
    private double avgNeutral;

    @Column(nullable = false)
    private String weeklyDetailedFeedback;

    @Column(nullable = false)
    private Date weekStartDate;

    @Column(nullable = false)
    private Date weekEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

}
