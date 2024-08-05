package com.jshop.infra.config.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BadRequestException extends RuntimeException implements ErrorException {
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(BadRequestCode errorCode) {
        super(errorCode.getMessage());
    }

    @Override
    public String getOutputMsg() {
        return BadRequestCode.BAD_REQUEST.getMessage();
    }
}
