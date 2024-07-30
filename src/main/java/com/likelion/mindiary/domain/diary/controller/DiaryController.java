package com.likelion.mindiary.domain.diary.controller;

import com.likelion.mindiary.domain.diary.controller.dto.request.AddDiaryRequest;
import com.likelion.mindiary.domain.diary.controller.dto.response.GetAllDiaryResponse;
import com.likelion.mindiary.domain.diary.controller.dto.response.GetMonthDiaryResponse;
import com.likelion.mindiary.domain.diary.model.Diary;
import com.likelion.mindiary.domain.diary.service.DiaryService;
import com.likelion.mindiary.global.Security.CustomUserDetails;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diary")
@CrossOrigin(origins = "http://localhost:3000")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<Object> analyzeDiary(
            @RequestBody @Valid AddDiaryRequest addDiaryRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Diary saveDiary = diaryService.addDiary(customUserDetails, addDiaryRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(saveDiary);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetAllDiaryResponse>> getAllDiaryWithFeedback(
            CustomUserDetails userDetails) {
        List<GetAllDiaryResponse> response =
                diaryService.getAllDiaryWithFeedback(userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/month")
    public ResponseEntity<List<GetMonthDiaryResponse>> getDiaryWithFeedbackByMonth(
            CustomUserDetails userDetails,
            @RequestParam int year,
            @RequestParam int month) {
        List<GetMonthDiaryResponse> response =
                diaryService.getMonthDiaryWithFeedback(userDetails, year, month);
        return ResponseEntity.ok(response);
    }
}
