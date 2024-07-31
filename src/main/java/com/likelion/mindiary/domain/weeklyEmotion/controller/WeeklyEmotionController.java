package com.likelion.mindiary.domain.weeklyEmotion.controller;

import com.likelion.mindiary.domain.weeklyEmotion.controller.dto.response.WeeklyEmotionResponse;
import com.likelion.mindiary.domain.weeklyEmotion.service.WeeklyEmotionService;
import com.likelion.mindiary.global.Security.CustomUserDetails;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weekly-emotion")
public class WeeklyEmotionController {

    private final WeeklyEmotionService weeklyEmotionService;

    @GetMapping
    public ResponseEntity<WeeklyEmotionResponse> getWeeklyEmotion(
            @RequestParam LocalDate endDate,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        WeeklyEmotionResponse response = weeklyEmotionService.getWeeklyEmotion(endDate,
                userDetails);
        return ResponseEntity.ok(response);
    }
}
