package com.jshop.infra.config.exception;

import lombok.Getter;

@Getter
public enum BadRequestCode implements ErrorCode {
    BAD_REQUEST("Bad request"),
    NOT_FOUND_RESOURCE("Not found resource"),
    RESOURCE_DUPLICATE("Resource duplicate"),
    PARAMETER_INVALID("Parameter invalid");

    private final String message;

    BadRequestCode(String message) {
        this.message = message;
    }
}
