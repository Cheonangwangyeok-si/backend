package com.likelion.mindiary.global.Security.JWT;

import com.likelion.mindiary.domain.account.model.Account;
import com.likelion.mindiary.domain.account.model.AccountRole;
import com.likelion.mindiary.domain.account.repository.AccountRepository;
import com.likelion.mindiary.domain.refreshToken.Repository.RefreshTokenRepository;
import com.likelion.mindiary.domain.refreshToken.model.RefreshToken;
import com.likelion.mindiary.global.Security.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String reponseURL = request.getRequestURI();
        System.out.println("reponseURL = " + reponseURL);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        if (reponseURL.equals("/api/v1/checkToken")) {
            if (checkRefreshToken(request)) {
                String refreshToken = request.getHeader("Refreshtoken").split(" ")[1];
                if (refreshToken != null) {
                    log.info("refreshToken 검증 시작");
                    System.out.println("refreshToken = " + refreshToken);

                    Optional<RefreshToken> storedRefreshToken = refreshTokenRepository.findByToken(refreshToken);

                    if (!validRefreshToken(storedRefreshToken.get())) {
                        setUnauthorizedResponse(response, "RefreshToken 만료");
                        filterChain.doFilter(request, response);
                        return;
                    }

                    Account member = storedRefreshToken.get().getAccount();
                    String role = "USER";
                    if (member.getRole() == AccountRole.ADMIN) role = "ADMIN";

                    String newToken = jwtUtil.createAccessToken(member.getLoginId(), role, 1000 * 100L);
                    System.out.println("newToken = " + newToken);
                    response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newToken);
                    setUnauthorizedResponse(response, "Access 토큰 발급");
                    return;
                }
            }

            try {
                jwtUtil.isExpired(token);
            } catch (ExpiredJwtException e) {
                System.out.println("!!!!!token expired!!!!!");
                setUnauthorizedResponse(response, "만료된 토큰");
                return;
            }
        }

        String loginId = jwtUtil.getLoginId(token);
        String role = jwtUtil.getRole(token);

        Account member = new Account();
        member.setLoginId(loginId);
        member.setRole(AccountRole.USER);

        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

    private void setUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write(message);
            response.getWriter().flush();
        }
    }

    public boolean validRefreshToken(RefreshToken storedRefreshToken) {
        try {
            jwtUtil.isExpired(storedRefreshToken.getToken());
        } catch (ExpiredJwtException e) {
            log.info("refreshToken 만료");
            return false;
        }
        log.info("refreshToken 검증 완료");
        return true;
    }

    public boolean checkRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refreshtoken");
        return refreshToken != null;
    }
}
