package com.likelion.mindiary.domain.account.controller;

import com.likelion.mindiary.domain.account.controller.dto.response.LoginDTO;
import com.likelion.mindiary.domain.account.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/account/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO){
        return accountService.login(loginDTO);
    }

    @GetMapping("/checkToken")
    public ResponseEntity<String> checkToken(){
        return ResponseEntity.ok().body("토큰 검증 완료");
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/account/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
        System.out.println("token = " + token);
        return accountService.logout(token);
    }
}
