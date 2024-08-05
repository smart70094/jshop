package com.jshop.infra.config.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ThrowableHandler {

    private final MethodArgumentNotValidExceptionService methodArgumentNotValidExceptionService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorVoRes httpRequestMethodNotSupportedException(Throwable e) {
        log.debug("Http request method not supported message: {}", e.getMessage(), e);

        return new ErrorVoRes(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorVoRes httpMessageNotReadableHandler(Throwable e) {
        log.debug("Http message not readable message: {}", e.getMessage(), e);

        return new ErrorVoRes(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ErrorVoRes authException(Throwable e) {
        log.error("Auth exception message: {}", e.getMessage(), e);

        return new ErrorVoRes(UnauthorizedCode.UNAUTHORIZED.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AccessDeniedException.class})
    public ErrorVoRes accountException(Throwable e) {
        log.error("Account exception message: {}", e.getMessage(), e);

        return new ErrorVoRes(UnauthorizedCode.ACCESS_DENIED.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorVoRes handleValidException(MethodArgumentNotValidException e) {
        String message = methodArgumentNotValidExceptionService.getMessage(e);
        log.error("Exception message: {}", message);
        log.info("Request: {}", e.getBindingResult().getTarget());

        return new ErrorVoRes(message);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    public ErrorVoRes error400(BadRequestException e) {
        String message = e.getMessage() == null ? e.getOutputMsg() : e.getMessage();
        log.error("Error400 message: {}", message, e);

        return new ErrorVoRes(message);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UnauthorizedException.class})
    public ErrorVoRes error401(UnauthorizedException e) {
        String message = e.getMessage() == null ? e.getOutputMsg() : e.getMessage();
        log.error("Error401 message: {}", message, e);

        return new ErrorVoRes(message);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({ForbiddenException.class})
    public ErrorVoRes error403(ForbiddenException e) {
        String message = e.getMessage() == null ? e.getOutputMsg() : e.getMessage();
        log.error("Error403 message: {}", message, e);

        return new ErrorVoRes(message);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class, DataAccessException.class, ConstraintViolationException.class})
    public ErrorVoRes databaseException(Throwable e) {
        log.error("Database exception message: {}", e.getMessage(), e);

        return new ErrorVoRes(InternalErrorErrorCode.DATABASE_ERROR.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    public ErrorVoRes error500(InternalServerErrorException e) {
        log.error("Error500 message: {}", e.getMessage(), e);

        return new ErrorVoRes(e.getOutputMsg());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorVoRes handleError(HttpServletRequest request, Throwable e) {
        String apiPath = getApiPath(request);
        log.error("Global exception, Api path: {}, message: {}", apiPath, e.getMessage(), e);

        return new ErrorVoRes(InternalErrorErrorCode.UNEXPECTED_ERROR);
    }

    private String getApiPath(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }
}