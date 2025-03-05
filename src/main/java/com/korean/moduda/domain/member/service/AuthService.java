package com.korean.moduda.domain.member.service;

import com.korean.moduda.domain.member.Member;
import com.korean.moduda.domain.member.Role;
import com.korean.moduda.domain.member.dto.LoginResponse;
import com.korean.moduda.domain.member.dto.SignUpRequest;
import com.korean.moduda.domain.member.repository.MemberRepository;
import com.korean.moduda.global.exception.BaseException;
import com.korean.moduda.global.exception.errorCode.MemberErrorCode;
import com.korean.moduda.global.security.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public void save(Member member) {
        memberRepository.save(member);
    }

    public LoginResponse login(String email, String password) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        Member member = memberOptional.orElseThrow(
            () -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BaseException(MemberErrorCode.INVALID_CREDENTIALS);
        }

        return jwtTokenProvider.generateToken(member.getId());
    }

    public void signup(SignUpRequest signupRequest) {
        if (memberRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new BaseException(MemberErrorCode.ALREADY_USED_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword()); // 비밀번호 암호화

        Member member = Member.builder()
            .email(signupRequest.getEmail())
            .name(signupRequest.getName())
            .password(encodedPassword)
            .role(Role.STUDENT)
            .build();

        save(member);
    }
}

