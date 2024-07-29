package com.likelion.mindiary.domain.diary.controller;

import com.likelion.mindiary.domain.diary.controller.dto.request.AddDiaryRequest;
import com.likelion.mindiary.domain.diary.controller.dto.response.GetAllDiaryResponse;
import com.likelion.mindiary.domain.diary.model.Diary;
import com.likelion.mindiary.domain.diary.service.DiaryService;
import com.likelion.mindiary.global.Security.CustomOauth2UserDetails;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<Object> analyzeDiary(
            @RequestBody @Valid AddDiaryRequest addDiaryRequest,
            @AuthenticationPrincipal CustomOauth2UserDetails customOauth2UserDetails) {

        Diary saveDiary = diaryService.addDiary(customOauth2UserDetails, addDiaryRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(saveDiary);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetAllDiaryResponse>> getDiaryWithFeedback(
            CustomOauth2UserDetails userDetails) {
        List<GetAllDiaryResponse> response =
                diaryService.getDiaryWithFeedback(userDetails);
        return ResponseEntity.ok(response);
    }
}
