package com.jshop.infra.config.bean;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAuditorBean implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Optional<SecurityContext> securityContextOptional = Optional.ofNullable(SecurityContextHolder.getContext());

        return securityContextOptional
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(User.class::isInstance)
                .map(principal -> ((User) principal).getUsername());
    }
}
