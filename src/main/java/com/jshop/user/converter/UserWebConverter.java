package com.jshop.user.converter;

import com.jshop.user.pojo.vo.LoginUserWebVoRes;
import org.springframework.stereotype.Component;

@Component
public class UserWebConverter {

    public LoginUserWebVoRes toLoginUserWebVoRes(String token) {
        return LoginUserWebVoRes.builder()
                .token(token)
                .build();
    }
}
