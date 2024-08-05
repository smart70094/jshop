package com.jshop.infra.config.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ForbiddenException extends RuntimeException implements ErrorException {
    public ForbiddenException(Throwable e) {
        super(e);
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public String getOutputMsg() {
        return ForbiddenCode.FORBIDDEN.getMessage();
    }
}
