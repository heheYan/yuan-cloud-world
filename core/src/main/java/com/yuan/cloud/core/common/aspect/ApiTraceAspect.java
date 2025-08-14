package com.yuan.cloud.core.common.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Description api方法调用跟踪切面
 *
 * @author Mr.Y
 * Created on 2025-08-12 10:31
 */
@Slf4j
@Aspect
@Component
@ConditionalOnWebApplication // 只在Web应用中启用
@ConditionalOnProperty( // 可通过配置控制是否启用
        name = "yuan.config.scan.enabled",
        havingValue = "true",
        matchIfMissing = true // 默认启用
)
public class ApiTraceAspect {

    @Around("execution(* com.yuan.cloud.*.*.api.*.*(..))")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取当前HTTP请求
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

            // 获取所有请求头名称
            Enumeration<String> headerNames = request.getHeaderNames();
            Map<String, String> headers = new HashMap<>();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                headers.put(headerName, headerValue);
            }

            log.info("请求参数：{}", joinPoint.getArgs());
            log.info("请求方法：{}", joinPoint.getSignature());
            log.info("请求类：{}", joinPoint.getTarget().getClass());
            log.info("请求方法名：{}", joinPoint.getSignature().getName());
            log.info("请求头：{}", headers);
        }
        return joinPoint.proceed();
    }

}
