package com.korean.moduda.global.security.jwt;

import com.korean.moduda.domain.member.dto.LoginResponse;
import com.korean.moduda.global.exception.BaseException;
import com.korean.moduda.global.exception.errorCode.AuthErrorCode;
import com.korean.moduda.global.security.auth.PrincipalDetails;
import com.korean.moduda.global.security.auth.PrincipalDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.jwt-key}")
    private String jwtSecretKey;

    @Value("${jwt.refresh-key}")
    private String refreshSecretKey;

    private final PrincipalDetailsService principalDetailsService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_HEADER = "RefreshToken";

    private static final long TOKEN_VALID_TIME = 1000 * 60L * 60L * 48L;
    private static final long REF_TOKEN_VALID_TIME = 1000 * 60L * 60L * 24L * 60L;  // 유효기간 2달

    @PostConstruct
    protected void init() {
        if (jwtSecretKey == null || refreshSecretKey == null) {
            throw new IllegalArgumentException("Secret keys must not be null");
        }
        jwtSecretKey = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
        refreshSecretKey = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes());
    }

    public String generateAccessToken(Long memberId) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);

        Date now = new Date();
        Date accessTokenExpirationTime = new Date(now.getTime() + TOKEN_VALID_TIME);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(accessTokenExpirationTime)
            .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
            .compact();
    }

    public LoginResponse generateToken(Long memberId) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);

        Date now = new Date();
        Date refreshTokenExpirationTime = new Date(now.getTime() + REF_TOKEN_VALID_TIME);

        String accessToken = generateAccessToken(memberId);
        String refreshToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(refreshTokenExpirationTime)
            .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
            .compact();

        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public Authentication getAuthentication(String token) {
        try {
            String memberId = getUserIdByToken(token);
            PrincipalDetails principalDetails = principalDetailsService.loadUserByUsername(memberId);
            return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
        } catch (UsernameNotFoundException exception) {
            throw new BaseException(AuthErrorCode.UNSUPPORTED_JWT);
        }
    }

    public Authentication getRefreshAuthentication(String token) {
        try {
            String memberId = getUserIdByRefreshToken(token);
            PrincipalDetails principalDetails = principalDetailsService.loadUserByUsername(memberId);
            return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
        } catch (UsernameNotFoundException exception) {
            throw new BaseException(AuthErrorCode.UNSUPPORTED_JWT);
        }
    }

    public String getUserIdByToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token)
            .getBody().get("memberId").toString();
    }

    public String getUserIdByRefreshToken(String token) {
        return Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(token)
            .getBody().get("memberId").toString();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        return request.getHeader(REFRESH_HEADER);
    }

    public boolean validateToken(String token) {
        return validateToken(token, jwtSecretKey);
    }

    private boolean validateToken(String token, String secretKey) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new BaseException(AuthErrorCode.INVALID_JWT);
        } catch (ExpiredJwtException e) {
            throw new BaseException(AuthErrorCode.EXPIRED_MEMBER_JWT);
        } catch (UnsupportedJwtException | SignatureException e) {
            throw new BaseException(AuthErrorCode.UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            throw new BaseException(AuthErrorCode.EMPTY_JWT);
        }
    }
}

