package com.likelion.mindiary.domain.diary.controller;

import com.likelion.mindiary.domain.diary.controller.dto.request.AddDiaryRequest;
import com.likelion.mindiary.domain.diary.controller.dto.response.GetAllDiaryResponse;
import com.likelion.mindiary.domain.diary.controller.dto.response.GetDiaryResponse;
import com.likelion.mindiary.domain.diary.controller.dto.response.GetMonthDiaryResponse;
import com.likelion.mindiary.domain.diary.model.Diary;
import com.likelion.mindiary.domain.diary.service.DiaryService;
import com.likelion.mindiary.global.Security.CustomUserDetails;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diary")
@CrossOrigin(origins = "http://localhost:3000")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<Object> addDiary(
            @RequestBody @Valid AddDiaryRequest addDiaryRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Diary saveDiary = diaryService.addDiary(userDetails, addDiaryRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(saveDiary);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetAllDiaryResponse>> getAllDiaryWithFeedback(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<GetAllDiaryResponse> response =
                diaryService.getAllDiaryWithFeedback(userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/month")
    public ResponseEntity<List<GetMonthDiaryResponse>> getDiaryWithFeedbackByMonth(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam int year,
            @RequestParam int month) {
        List<GetMonthDiaryResponse> response =
                diaryService.getMonthDiaryWithFeedback(userDetails, year, month);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiaryByDiaryId(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long diaryId) {

        diaryService.deleteDiary(userDetails, diaryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<GetDiaryResponse> getDiaryByDiaryId(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long diaryId) {

        GetDiaryResponse response = diaryService.getDiaryByDiaryId(userDetails, diaryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/missing-days")
    public ResponseEntity<List<LocalDate>> getMissingDiaryDays(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<LocalDate> missingDays = diaryService.getMissingDiaryDays(userDetails);
        return ResponseEntity.ok(missingDays);
    }
}
