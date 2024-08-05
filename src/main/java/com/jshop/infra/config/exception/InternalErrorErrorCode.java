package com.jshop.infra.config.exception;

import lombok.Getter;

@Getter
public enum InternalErrorErrorCode implements ErrorCode {
    INTERNAL_ERROR("Internal error"),
    DATABASE_ERROR("Database error"),
    NO_DATA("Should be exists data, but not found"),
    UNEXPECTED_ERROR("Unexpected error");

    private final String message;

    InternalErrorErrorCode(String message) {
        this.message = message;
    }

}
