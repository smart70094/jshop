package com.jshop.infra.config.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InternalServerErrorException extends RuntimeException implements ErrorException {
    public InternalServerErrorException(Throwable e) {
        super(e);
    }

    public InternalServerErrorException(InternalErrorErrorCode errorCode500) {
        super(errorCode500.getMessage());
    }

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getOutputMsg() {
        return InternalErrorErrorCode.INTERNAL_ERROR.getMessage();
    }
}

