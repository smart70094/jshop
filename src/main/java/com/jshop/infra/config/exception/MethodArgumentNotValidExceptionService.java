package com.jshop.infra.config.exception;

import jakarta.validation.constraints.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class MethodArgumentNotValidExceptionService {
    public String getMessage(MethodArgumentNotValidException methodArgumentNotValidException) {
        try {
            return parseMessage(methodArgumentNotValidException);
        } catch (Exception e) {
            log.error("parse error failure.", e);
            return "解析錯誤失敗!";
        }
    }

    private String parseMessage(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getFieldError();
        String field = Objects.requireNonNull(fieldError).getField();
        int annotationIndex = Optional.ofNullable(fieldError.getCodes()).map(codes -> codes.length - 1).orElse(0);
        String annotation = Objects.requireNonNull(fieldError.getCodes())[annotationIndex];
        Object[] arguments = Objects.requireNonNull(fieldError.getArguments());

        if (isEqualToAnnotation(NotNull.class, annotation)) {
            return String.format("%s cannot be null", field);
        } else if (isEqualToAnnotation(NotBlank.class, annotation)) {
            return String.format("%s cannot be null or empty", field);
        } else if (isEqualToAnnotation(Range.class, annotation)) {
            return String.format("%s must be between %s and %s", field, arguments[2], arguments[1]);
        } else if (isEqualToAnnotation(Size.class, annotation)) {
            return String.format("%s collection size must be between %s and %s", field, arguments[2], arguments[1]);
        } else if (isEqualToAnnotation(NotEmpty.class, annotation)) {
            return String.format("%s cannot be empty collection", field);
        } else if (isEqualToAnnotation(Max.class, annotation)) {
            return String.format("%s must be less than or equal to %s", field, arguments[1]);
        } else if (isEqualToAnnotation(Min.class, annotation)) {
            return String.format("%s must be greater than or equal to %s", field, arguments[1]);
        } else if (isEqualToAnnotation(Positive.class, annotation)) {
            return String.format("%s should be positive", field);
        } else if (isEqualToAnnotation(PositiveOrZero.class, annotation)) {
            return String.format("%s should be 0 or positive", field);
        } else if (isEqualToAnnotation(Negative.class, annotation)) {
            return String.format("%s should be negative", field);
        } else if (isEqualToAnnotation(NegativeOrZero.class, annotation)) {
            return String.format("%s should be 0 or negative", field);
        } else if (isEqualToAnnotation(Future.class, annotation)) {
            return String.format("%s should be a date in the future", field);
        } else if (isEqualToAnnotation(FutureOrPresent.class, annotation)) {
            return String.format("%s should be a date in the future or present", field);
        } else if (isEqualToAnnotation(Past.class, annotation)) {
            return String.format("%s should be a date in the past", field);
        } else if (isEqualToAnnotation(PastOrPresent.class, annotation)) {
            return String.format("%s should be a date in the past or present", field);
        }

        return annotation;
    }

    private boolean isEqualToAnnotation(Class<?> clazz, String annotation) {
        return clazz.getName().endsWith(annotation);
    }
}
