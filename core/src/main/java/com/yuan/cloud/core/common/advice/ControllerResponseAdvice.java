package com.yuan.cloud.core.common.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.cloud.core.common.exception.YuanApiException;
import com.yuan.cloud.core.common.response.YuanR;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author YuAN
 * Created on 2025-05-14 23:28
 * @description 接口返回格式统一封装
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ControllerResponseAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        // 返回值类型不是YuanR类型的才拦截
        return !returnType.getParameterType().isAssignableFrom(YuanR.class);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @Nonnull MethodParameter returnType,
                                  @Nonnull MediaType selectedContentType,
                                  @Nonnull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @Nonnull ServerHttpRequest request,
                                  @Nonnull ServerHttpResponse response) {
        // 对 null 做空对象兼容处理
        if (body == null) {
            return YuanR.ok();
        }
        // 针对String类型返回值做特殊处理
        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(YuanR.ok(body));
            } catch (JsonProcessingException e) {
                throw new YuanApiException(e.getMessage());
            }
        }
        return YuanR.ok(body);
    }
}