package com.korean.moduda.global.security.jwt;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korean.moduda.global.exception.BaseException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BaseException exception) {
            setErrorResponse(response, exception);
        }
    }

    public void setErrorResponse(HttpServletResponse res, BaseException exception)
        throws IOException {
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setStatus(exception.getStatus().value());
        final Map<String, Object> body = new HashMap<>();
        body.put("code", exception.getErrorCode());
        body.put("message", exception.getMessage());
        body.put("timestamp", LocalDateTime.now().toString());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(res.getOutputStream(), body);
    }
}
