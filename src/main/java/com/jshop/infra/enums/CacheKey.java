package com.jshop.infra.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheKey {
    LOGIN_ATTEMPTS("login:attempts");

    private String key;
}
