package com.likelion.mindiary.domain.account.controller;

import com.likelion.mindiary.domain.account.controller.dto.request.LoginRequest;
import com.likelion.mindiary.domain.account.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/account/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return accountService.login(loginRequest);
    }

    @GetMapping("/checkToken")
    public ResponseEntity<String> checkToken() {
        return ResponseEntity.ok().body("토큰 검증 완료");
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/account/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
        System.out.println("token = " + token);
        return accountService.logout(token);
    }
}
