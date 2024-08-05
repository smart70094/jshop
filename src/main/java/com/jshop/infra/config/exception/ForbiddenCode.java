package com.jshop.infra.config.exception;

import lombok.Getter;

@Getter
public enum ForbiddenCode implements ErrorCode {
    FORBIDDEN("Forbidden");

    private final String message;

    ForbiddenCode(String message) {
        this.message = message;
    }
}
