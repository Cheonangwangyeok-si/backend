package com.likelion.mindiary.global.Security.JWT;

import com.likelion.mindiary.domain.account.model.Account;
import com.likelion.mindiary.domain.account.model.AccountRole;
import com.likelion.mindiary.domain.account.repository.AccountRepository;
import com.likelion.mindiary.domain.refreshToken.Repository.RefreshTokenRepository;
import com.likelion.mindiary.domain.refreshToken.model.RefreshToken;
import com.likelion.mindiary.global.Security.CustomOauth2UserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private final RefreshTokenRepository refreshTokenRepository;

    private final AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request에서 Authorization 헤더 찾음
        String authorization = request.getHeader("Authorization");



        // Authorization 헤더 검증
        // Authorization 헤더가 비어있거나 "Bearer " 로 시작하지 않은 경우
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("\n\n\ntoken null");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/plain; charset=UTF-8");  // Ensure the correct content type and character encoding
            response.getWriter().write("token null");
            // 토큰이 유효하지 않으므로 request와 response를 다음 필터로 넘겨줌
            filterChain.doFilter(request, response);

            // 메서드 종료
            return;
        }

        // Authorization에서 Bearer 접두사 제거
        String token = authorization.split(" ")[1];

        if (checkRefreshToken(request) == true) {
            String refreshToken = request.getHeader("Refreshtoken").split(" ")[1];
            if (refreshToken != null) {
                log.info("refreshToken 검증 시작");
                System.out.println("refreshToken = " + refreshToken);

                Optional<RefreshToken> storedRefreshToken = refreshTokenRepository.findByToken(refreshToken);

                if (!validRefreshToken(storedRefreshToken.get())) { //refreshToken도 만료


                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("text/plain; charset=UTF-8");  // Ensure the correct content type and character encoding
                    response.getWriter().write("RefreshToken 만료");
                    filterChain.doFilter(request, response);

                    // 메서드 종료
                    return;
                }
                Account member = storedRefreshToken.get().getAccount();
                // 검증 성고 시 access 토큰 재발급
                String role = "USER";
                if (member.getRole() == AccountRole.ADMIN)  role = "ADMIN";

                String newToken = jwtUtil.createAccessToken(member.getLoginId(), role, 1000 * 100L);
                System.out.println("newToken = " + newToken);
                response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newToken);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("text/plain; charset=UTF-8");  // Ensure the correct content type and character encoding
                response.getWriter().write("Access 토큰 발급");
                response.getWriter().flush();
                return;
            }
        }

        // token 소멸 시간 검증
        // 유효기간이 만료한 경우
        // refresh 토큰 요청 후 db에 저장된 refresh토큰과 비교 -> 일치하면 access토큰 재발급
        try {
            jwtUtil.isExpired(token);

        } catch (ExpiredJwtException e) {
            System.out.println("!!!!!token expired!!!!!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/plain; charset=UTF-8");  // Ensure the correct content type and character encoding
            response.getWriter().write("만료된 토큰");
            response.getWriter().flush();
            return;
        }

        // 최종적으로 token 검증 완료 => 일시적인 session 생성
        // session에 user 정보 설정
        String loginId = jwtUtil.getLoginId(token);

        String role = jwtUtil.getRole(token);

        Account member = new Account();
        member.setLoginId(loginId);
        // 매번 요청마다 DB 조회해서 password 초기화 할 필요 x => 정확한 비밀번호 넣을 필요 없음
        // 따라서 임시 비밀번호 설정!
        member.setRole(AccountRole.USER);

        // UserDetails에 회원 정보 객체 담기
        CustomOauth2UserDetails customUserDetails = new CustomOauth2UserDetails(member);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 세션에 사용자 등록 => 일시적으로 user 세션 생성
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 request, response 넘겨줌
        filterChain.doFilter(request, response);
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
        if(refreshToken == null) return false;
        return true;
    }
}
