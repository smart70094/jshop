package com.jshop.infra.config.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

@Aspect
@Configuration
@Slf4j
public class LogAopConfig {

    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;

    public LogAopConfig(HttpServletRequest request, ObjectMapper objectMapper) {
        this.request = request;
        this.objectMapper = objectMapper;
    }

    @Pointcut("bean(*Controller)")
    public void logPointCut() {
        // cut log point config
    }

    @Around(value = "logPointCut()", argNames = "pjp")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String className = getClassName(pjp.getSignature().getDeclaringTypeName());
        String methodName = signature.getMethod().getName();
        String parameters = getParameters(pjp.getArgs());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        MDC.put("uuid", uuid);

        log.info("Request：[{}] {}, parameter={}", className, methodName, parameters);
        logAuthorization(request);
        Object response = process(pjp);
        log.info("Response：{}", objectMapper.writeValueAsString(response));

        MDC.clear();
        return response;
    }

    private Object process(ProceedingJoinPoint pjp) throws Throwable {
        Object response;
        try {
            response = pjp.proceed();
        } catch (Exception e) {
            log.error("Error：{}", e, e);
            throw e;
        }

        return response;
    }

    private void logAuthorization(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(authorization)) {
            return;
        }

        log.info("Request authorization：{}", authorization);
    }

    private String getClassName(String packageName) {
        String[] packageNames = packageName.split("\\.");
        LinkedList<String> packageNameList = Lists.newLinkedList(Arrays.asList(packageNames));
        return packageNameList.getLast();
    }

    private String getParameters(Object[] args) throws JsonProcessingException {
        StringBuilder parameters = new StringBuilder();
        for (Object arg : args) {
            if (arg instanceof MultipartFile) {
                continue;
            }
            String parameter = objectMapper.writeValueAsString(arg);
            parameters.append(parameter).append(" ");
        }
        return parameters.toString();
    }
}