package com.korean.moduda.global.exception.errorCode;

import com.korean.moduda.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LectureErrorCode implements ErrorCode {
    LECTURE_NOT_FOUND("LECTURE_001", "존재하지 않는 강의입니다.", HttpStatus.BAD_REQUEST),
    LECTURE_DETAIL_NOT_FOUND("LECTURE_002", "상세정보가 존재하지 않는 강의입니다.", HttpStatus.BAD_REQUEST),
    INVALID_LECTURE_TYPE("LECTURE_003", "강의 종류가 올바르지 않습니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    ;
    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
