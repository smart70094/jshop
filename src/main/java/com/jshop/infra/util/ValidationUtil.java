package com.jshop.infra.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {

    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (CollectionUtils.isNotEmpty(violations)) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            throw new IllegalArgumentException("Validation failed: \n" + sb);
        }
    }
}
