package com.jshop.user.controller;

import com.jshop.user.pojo.vo.LoginUserWebVoReq;
import com.jshop.user.pojo.vo.LoginUserWebVoRes;
import com.jshop.user.service.UserWebService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/web/user")
public class UserCmsController {

    private final UserWebService userWebService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/login")
    public LoginUserWebVoRes login(@Valid @RequestBody LoginUserWebVoReq request) {
        return userWebService.login(request);
    }
}
