package com.likelion.mindiary.domain.diary.model;

public enum Emotion {
    HAPPINESS("행복"),
    ANGER("분노"),
    SADNESS("슬픔"),
    SURPRISE("놀람"),
    NEUTRAL("중립"),;

    private final String description;

    Emotion(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

}
