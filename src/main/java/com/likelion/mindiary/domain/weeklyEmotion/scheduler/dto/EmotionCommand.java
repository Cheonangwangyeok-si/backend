package com.likelion.mindiary.domain.weeklyEmotion.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmotionCommand {
    private int happiness;
    private int sadness;
    private int anger;
    private int surprise;
    private int neutral;
}
