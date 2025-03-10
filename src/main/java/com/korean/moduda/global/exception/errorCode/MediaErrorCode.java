package com.korean.moduda.global.exception.errorCode;

import com.korean.moduda.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MediaErrorCode implements ErrorCode {
    INVALID_MEDIA("MEDIA_001", "존재하지 않는 파일입니다.", HttpStatus.BAD_REQUEST),
    AUTHORIZATION_ERROR("MEDIA_002", "해당 파일의 수정 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    ;
    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
