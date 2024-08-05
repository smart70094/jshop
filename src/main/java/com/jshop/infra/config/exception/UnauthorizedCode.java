package com.jshop.infra.config.exception;

import lombok.Getter;

@Getter
public enum UnauthorizedCode implements ErrorCode {
    UNAUTHORIZED("Unauthorized"),
    ACCESS_DENIED("Access denied");

    private final String message;

    UnauthorizedCode(String message) {
        this.message = message;
    }
}
