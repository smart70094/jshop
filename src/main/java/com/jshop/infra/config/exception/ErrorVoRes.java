package com.jshop.infra.config.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorVoRes {
    private String message;

    public ErrorVoRes(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }
}
