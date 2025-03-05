package com.korean.moduda.global.exception.errorCode;

import com.korean.moduda.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND("MEMBER_001", "존재하지 않는 사용자입니다.", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS("MEMBER_002","이메일 또는 비밀번호가 잘못되었습니다.",HttpStatus.BAD_REQUEST),
    ALREADY_USED_EMAIL("MEMBER_003","이미 사용중인 이메일입니다.",HttpStatus.BAD_REQUEST),
    ;
    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
