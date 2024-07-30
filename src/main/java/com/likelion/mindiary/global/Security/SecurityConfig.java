package com.likelion.mindiary.global.Security;

import com.likelion.mindiary.domain.account.repository.AccountRepository;
import com.likelion.mindiary.domain.account.service.CustomUserDetailsService;
import com.likelion.mindiary.domain.refreshToken.Repository.RefreshTokenRepository;
import com.likelion.mindiary.global.Security.JWT.JWTFilter;
import com.likelion.mindiary.global.Security.JWT.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration configuration;
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountRepository memberRepository;

    private final AccessDeniedHandler customAccessDeniedHandler;
    private final AuthenticationEntryPoint customAuthenticationEntryPoint;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .cors(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/error/**", "/actuator/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/v1/diary").permitAll()
                        .anyRequest().authenticated());

        http
                .formLogin((auth) -> auth.loginPage("/api/v1/account/login")
                        .loginProcessingUrl("/test")
                        .permitAll()
                );
//        http
//                .logout((auth) -> auth
//                        .logoutUrl("/api/v1/account/logout")
//                );

//        // 세션설정
//        http
//                .sessionManagement((session) -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 새로 만든 로그인 필터를 원래의 (UsernamePasswordAuthenticationFilter)의 자리에 넣음
        http
                .addFilterAt(new LoginFilter(authenticationManager(configuration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // 로그인 필터 이전에 JWTFilter를 넣음
        http
                .addFilterBefore(new JWTFilter(jwtUtil, refreshTokenRepository, memberRepository), LoginFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
