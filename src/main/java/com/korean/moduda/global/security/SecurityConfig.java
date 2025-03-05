package com.korean.moduda.global.security;

import com.korean.moduda.global.security.jwt.JwtAuthenticationFilter;
import com.korean.moduda.global.security.jwt.JwtExceptionFilter;
import com.korean.moduda.global.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // CSRF 보호 비활성화
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않도록 설정
            .and()
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll() // 다른 모든 요청은 허용
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
            .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class); // JWT 예외 필터 추가

        return http.build(); // SecurityFilterChain 객체 반환
    }
}

