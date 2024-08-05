package com.jshop.infra.config.security;


import com.jshop.user.domain.User;
import com.jshop.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityHolder {
    private static final String ANONYMOUS_USER = "anonymousUser";
    private final UserRepository userRepository;

    public SecurityHolder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ANONYMOUS_USER.equals(authentication.getPrincipal())) {
            return null;
        }

        return userRepository.findByUsername(getUserName()).orElse(null);
    }

    private String getUserName() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }
}