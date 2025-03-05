package com.korean.moduda.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Refresh Token 처리
        String refreshToken = jwtTokenProvider.resolveRefreshToken(httpRequest);
        if (isRefreshTokenRequest(httpRequest, refreshToken)) {
            handleRefreshToken(refreshToken);
        } else {
            // JWT 토큰 처리
            String token = jwtTokenProvider.resolveToken(httpRequest);
            if (isTokenInvalid(token, httpRequest)) {
                SecurityContextHolder.getContext().setAuthentication(null);
            } else if (token != null && jwtTokenProvider.validateToken(token)) {
                handleAccessToken(token);
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isRefreshTokenRequest(HttpServletRequest request, String refreshToken) {
        return refreshToken != null && "/api/v1/auth/refresh".equals(request.getRequestURI());
    }

    private void handleRefreshToken(String refreshToken) {
        Authentication authentication = jwtTokenProvider.getRefreshAuthentication(refreshToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean isTokenInvalid(String token, HttpServletRequest request) {
        return Objects.equals(token, "");
    }

    private void handleAccessToken(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
