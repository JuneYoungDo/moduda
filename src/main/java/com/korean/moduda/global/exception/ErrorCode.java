package com.korean.moduda.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getErrorCode();

    String getMessage();

    HttpStatus getStatus();
}
