package com.korean.moduda.domain.member.controller;

import com.korean.moduda.domain.member.dto.LoginRequest;
import com.korean.moduda.domain.member.dto.LoginResponse;
import com.korean.moduda.domain.member.dto.SignUpRequest;
import com.korean.moduda.domain.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse tokenInfo = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(tokenInfo);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest signupRequest) {
        authService.signup(signupRequest);
        return ResponseEntity.ok("200");
    }
}

