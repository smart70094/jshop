package com.jshop.infra.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jshop.infra.config.exception.ErrorVoRes;
import com.jshop.infra.config.exception.ForbiddenCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    @Value("${project.permitUrl}")
    private String permitUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = getAuthorizationHeader(request);
        String servletPath = request.getServletPath();

        if (isPermitUrl(servletPath)) {
            chain.doFilter(request, response);
            return;
        }

        if (isNotValidateBearerAuthorization(authorizationHeader)) {
            logger.warn("JWT Token is empty or not start with Bearer:" + authorizationHeader);
            throwError401(response);
            return;
        }

        String username;
        String jwtToken = authorizationHeader.substring(BEARER_PREFIX.length());

        try {
            username = jwtService.getUsernameFromToken(jwtToken);
        } catch (Exception e) {
            log.error("JWT exception message: {}", e.getMessage(), e);
            throwError401(response);
            return;
        }

        try {
            initialSecurityHolder(username, request);
        } catch (UsernameNotFoundException e) {
            log.error("JWT exception message: {}", e.getMessage(), e);
            throwError401(response);
            return;
        }

        chain.doFilter(request, response);
    }

    private void throwError401(HttpServletResponse response) throws IOException {
        ErrorVoRes errorVoRes = new ErrorVoRes(ForbiddenCode.FORBIDDEN);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        PrintWriter writer = response.getWriter();
        writer.print(objectMapper.writeValueAsString(errorVoRes));
        writer.flush();
    }

    private String getAuthorizationHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return StringUtils.isBlank(authorizationHeader) ? "" : authorizationHeader;
    }

    private boolean isPermitUrl(String servletPath) {
        return Arrays.stream(permitUrl.split(","))
                .anyMatch(pattern -> new AntPathMatcher().match(pattern, servletPath));
    }

    private boolean isNotValidateBearerAuthorization(String authorizationHeader) {
        return ObjectUtils.isEmpty(authorizationHeader) ||
                BooleanUtils.isFalse(authorizationHeader.startsWith(BEARER_PREFIX));
    }

    private void initialSecurityHolder(String username, HttpServletRequest request) {
        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}