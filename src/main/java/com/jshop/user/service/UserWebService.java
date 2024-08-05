package com.jshop.user.service;

import com.jshop.infra.config.exception.BadRequestException;
import com.jshop.infra.config.exception.ForbiddenException;
import com.jshop.infra.config.security.JwtService;
import com.jshop.user.converter.UserWebConverter;
import com.jshop.user.dao.LoginUserCacheDao;
import com.jshop.user.domain.User;
import com.jshop.user.pojo.vo.LoginUserWebVoReq;
import com.jshop.user.pojo.vo.LoginUserWebVoRes;
import com.jshop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWebService {
    private final UserRepository userRepository;
    private final UserWebConverter userWebConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LoginUserCacheDao loginUserCacheDao;

    private static final String INVALID_USERNAME_OR_PASSWORD_MESSAGE = "Invalid login credentials. Please check your username and password.";

    public LoginUserWebVoRes login(LoginUserWebVoReq request) {
        String username = request.getUsername();

        if (loginUserCacheDao.getLoginAttemptCount(username) >= 3) {
            throw new ForbiddenException("Your account is locked due to too many failed login attempts. Please try again later.");
        }

        User user = getUser(username);

        checkPassword(request, username, user);

        String token = jwtService.generateToken(username);

        loginUserCacheDao.deleteLoginAttemptCount(username);

        return userWebConverter.toLoginUserWebVoRes(token);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            loginUserCacheDao.addLoginAttemptCount(username);
            return new BadRequestException(INVALID_USERNAME_OR_PASSWORD_MESSAGE);
        });
    }

    private void checkPassword(LoginUserWebVoReq request, String username, User user) {
        if (BooleanUtils.isFalse(passwordEncoder.matches(request.getPassword(), user.getPassword()))) {
            loginUserCacheDao.addLoginAttemptCount(username);
            throw new BadRequestException(INVALID_USERNAME_OR_PASSWORD_MESSAGE);
        }
    }
}
