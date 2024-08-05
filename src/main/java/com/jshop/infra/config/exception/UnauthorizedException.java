package com.jshop.infra.config.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnauthorizedException extends RuntimeException implements ErrorException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public String getOutputMsg() {
        return UnauthorizedCode.UNAUTHORIZED.getMessage();
    }
}
