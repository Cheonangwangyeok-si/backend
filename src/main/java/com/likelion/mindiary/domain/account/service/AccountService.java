package com.likelion.mindiary.domain.account.service;

import com.likelion.mindiary.domain.account.controller.dto.request.LoginRequest;
import com.likelion.mindiary.domain.account.model.Account;
import com.likelion.mindiary.domain.account.model.AccountRole;
import com.likelion.mindiary.domain.account.repository.AccountRepository;
import com.likelion.mindiary.domain.refreshToken.Repository.RefreshTokenRepository;
import com.likelion.mindiary.domain.refreshToken.model.RefreshToken;
import com.likelion.mindiary.global.Security.JWT.JWTUtil;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final JWTUtil jwtUtil;
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public ResponseEntity<String> login(LoginRequest loginDTO){

        Account findAccount = accountRepository.findByLoginId(loginDTO.getLoginId());
        Account account;

        if(findAccount == null){ // 회원가입
            account = Account.builder()
                    .loginId(loginDTO.getLoginId())
                    .name(loginDTO.getName())
                    .provider("google")
                    .role(AccountRole.USER)
                    .build();
            accountRepository.save(account);

        } else {
            account = findAccount;
        }

        //refresh token 있다면 삭제 후 재발급
        Optional<RefreshToken> postRefreshToken = refreshTokenRepository.findByAccount(account);
        if(postRefreshToken.isPresent()){
            RefreshToken refreshToken = postRefreshToken.get();
            refreshTokenRepository.delete(refreshToken);
            refreshTokenRepository.flush();
        }

        String role = "USER";
        if (account.getRole() == AccountRole.ADMIN) role = "ADMIN";

        //String accessToken = jwtUtil.createAccessToken(account.getLoginId(), role, 1000 * 10L);
        String accessToken = jwtUtil.createAccessToken(account.getLoginId(), role, 1000 * 60 * 60 * 24 * 7L);
        String refreshToken = jwtUtil.createRefreshToken(1000*60*60*24*7L ); // 1달


        RefreshToken refreshToken1 = RefreshToken.builder()
                .account(account)
                .token(refreshToken)
                .build();

        refreshTokenRepository.save(refreshToken1);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header("RefreshToken", "Bearer " + refreshToken)
                .build();
    }

    public ResponseEntity<String> logout(String accessToken) {
        String loginId = jwtUtil.getLoginId(accessToken);
        Account member = accountRepository.findByLoginId(loginId);
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccount(member);
        if(!refreshToken.isPresent()){
            return ResponseEntity.badRequest().body("유효하지 않은 토큰");
        }
        System.out.println("refreshToken = " + refreshToken.get().getToken());
        refreshTokenRepository.delete(refreshToken.get());
        refreshTokenRepository.flush();
        return ResponseEntity.ok("refreshToken 삭제");
    }

}
