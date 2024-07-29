package com.likelion.mindiary.domain.account.controller;

import com.likelion.mindiary.domain.account.service.AccountService;
import com.likelion.mindiary.global.Security.CustomOauth2UserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/account/login")
    public ResponseEntity<String> login(@AuthenticationPrincipal CustomOauth2UserDetails customOauth2UserDetails){
        log.info("user = {}",customOauth2UserDetails.getAttributes());
        return accountService.login(customOauth2UserDetails);
    }

    @GetMapping("/test")
    public String test(){
        return "테스트";
    }

    @GetMapping("/account/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
        return accountService.logout(token);
    }
}
