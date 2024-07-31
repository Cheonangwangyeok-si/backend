CREATE TABLE IF NOT EXISTS account
(
    account_id BIGINT       NOT NULL AUTO_INCREMENT,
    login_id   VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL,
    provider   VARCHAR(255),
    PRIMARY KEY (account_id)
);

CREATE TABLE IF NOT EXISTS diary
(
    diary_id     BIGINT       NOT NULL AUTO_INCREMENT,
    title        VARCHAR(255) NOT NULL,
    content      TEXT         NOT NULL,
    diary_at     DATE         NOT NULL,
    emotion_type VARCHAR(20)  NOT NULL,
    account_id   BIGINT       NOT NULL,
    PRIMARY KEY (diary_id),
    FOREIGN KEY (account_id) REFERENCES account (account_id)
);

CREATE TABLE IF NOT EXISTS daily_emotion
(
    daily_emotion_id  BIGINT NOT NULL AUTO_INCREMENT,
    happiness         INT    NOT NULL,
    sadness           INT    NOT NULL,
    anger             INT    NOT NULL,
    surprise          INT    NOT NULL,
    neutral           INT    NOT NULL,
    detailed_feedback TEXT   NOT NULL,
    short_feedback    TEXT   NOT NULL,
    diary_id          BIGINT NOT NULL,
    PRIMARY KEY (daily_emotion_id),
    FOREIGN KEY (diary_id) REFERENCES diary (diary_id)
);

CREATE TABLE IF NOT EXISTS weekly_emotion
(
    weekly_emotion_id        BIGINT NOT NULL AUTO_INCREMENT,
    avg_happiness            DOUBLE NOT NULL,
    avg_anger                DOUBLE NOT NULL,
    avg_sadness              DOUBLE NOT NULL,
    avg_surprise             DOUBLE NOT NULL,
    avg_neutral              DOUBLE NOT NULL,
    weekly_detailed_feedback TEXT   NOT NULL,
    week_start_date          DATE   NOT NULL,
    week_end_date            DATE   NOT NULL,
    account_id               BIGINT NOT NULL,
    PRIMARY KEY (weekly_emotion_id),
    FOREIGN KEY (account_id) REFERENCES account (account_id)
);

CREATE TABLE IF NOT EXISTS refresh_token
(
    token_id   BIGINT       NOT NULL AUTO_INCREMENT,
    token      VARCHAR(255) NOT NULL,
    account_id BIGINT       NOT NULL,
    PRIMARY KEY (token_id),
    FOREIGN KEY (account_id) REFERENCES account (account_id)
);