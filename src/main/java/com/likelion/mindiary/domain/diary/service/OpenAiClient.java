package com.likelion.mindiary.domain.diary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.mindiary.domain.dailyEmotion.model.DailyEmotion;
import com.likelion.mindiary.domain.dailyEmotion.repository.DailyEmotionRepository;
import com.likelion.mindiary.domain.diary.controller.dto.request.OpenAiRequest;
import com.likelion.mindiary.domain.diary.controller.dto.response.OpenAiResponse;
import com.likelion.mindiary.domain.diary.exception.EmotionAnalysisFailedException;
import com.likelion.mindiary.domain.diary.exception.ResponseParseFailedException;
import com.likelion.mindiary.domain.diary.model.Diary;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class OpenAiClient {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiClient.class);

    private final DailyEmotionRepository dailyEmotionRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${openai.url}")
    private String openAiUrl;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.prompt}")
    private String prompt;

    public void analyzeEmotion(String content, Diary savedDiary) {

        URI uri = UriComponentsBuilder
                .fromUriString(openAiUrl)
                .build()
                .encode()
                .toUri();

        OpenAiRequest.MessageDto messageDto = new OpenAiRequest.MessageDto("user", prompt + content);
        OpenAiRequest requestDto = new OpenAiRequest(model, List.of(messageDto));
        RequestEntity<OpenAiRequest> httpEntity = new RequestEntity<>(requestDto, HttpMethod.POST, uri);

        try {
            ResponseEntity<String> exchange = restTemplate.exchange(httpEntity, String.class);
            String responseBody = exchange.getBody();

            OpenAiResponse openAiResponse = extractEmotionFromResponse(responseBody);

            DailyEmotion newDiaryEmotion = DailyEmotion.builder()
                    .happiness(openAiResponse.happiness())
                    .sadness(openAiResponse.sadness())
                    .anger(openAiResponse.anger())
                    .surprise(openAiResponse.surprise())
                    .neutral(openAiResponse.neutral())
                    .detailedFeedback(openAiResponse.detailedFeedback())
                    .shortFeedback(openAiResponse.shortFeedback())
                    .diary(savedDiary)
                    .build();

            dailyEmotionRepository.save(newDiaryEmotion);

        } catch (Exception e) {
            logger.error("Failed to analyze emotion", e);
            throw new EmotionAnalysisFailedException();
        }
    }

    private OpenAiResponse extractEmotionFromResponse(String responseBody) {
        try {
            JsonNode jsonResponse = objectMapper.readTree(responseBody);

            String analysisResult = jsonResponse
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText()
                    .trim();

            JsonNode analysisJson = objectMapper.readTree(analysisResult);

            String shortFeedback = analysisJson.get("shortFeedback").asText();
            String detailedFeedback = analysisJson.get("detailedFeedback").asText();
            int happiness = analysisJson.get("happiness").asInt();
            int sadness = analysisJson.get("sadness").asInt();
            int anger = analysisJson.get("anger").asInt();
            int surprise = analysisJson.get("surprise").asInt();
            int neutral = analysisJson.get("neutral").asInt();

            return new OpenAiResponse(shortFeedback, detailedFeedback, happiness, sadness, anger, surprise, neutral);

        } catch (Exception e) {
            logger.error("Failed to parse emotion analysis response", e);
            throw new ResponseParseFailedException();
        }
    }
}
