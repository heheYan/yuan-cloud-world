package com.yuan.cloud.core.common.exception.handler;

import com.alibaba.fastjson2.JSONObject;
import com.yuan.cloud.core.common.exception.YuanApiException;
import com.yuan.cloud.core.common.response.YuanR;
import feign.FeignException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.sasl.AuthenticationException;
import java.util.Objects;

import static com.yuan.cloud.core.common.enums.YuanStatusEnum.*;

/**
 * @author YuAN
 * Created on 2025-03-10 09:54
 * description 全局异常捕获处理类
 */
@Slf4j
@RestControllerAdvice
@Tag(name = "YuanWebExceptionHandler", description = "全局异常捕获处理类")
public class YuanWebExceptionHandler {
    /**
     * 自定义异常捕获
     *
     * @param e 自定义异常
     * @return YuanR
     */
    @ExceptionHandler(YuanApiException.class)
    public YuanR<String> handleYuanApiException(YuanApiException e) {
        return YuanR.fail(e.getErrEnum(), e.getMessage());
    }

    /**
     * 参数校验异常捕获
     *
     * @param e 异常
     * @return YuanR
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public YuanR<JSONObject> handleArgsNotValidException(MethodArgumentNotValidException e) {
        JSONObject errData = new JSONObject();
        if (!e.getBindingResult().getFieldErrors().isEmpty()) {
            for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
                errData.put(fieldError.getField(), Objects.requireNonNull(fieldError.getDefaultMessage()));
            }
        }
        return YuanR.fail(REQUEST_PARAM_VALID_ERROR, errData);
    }

    /**
     * 权限不足异常捕获
     *
     * @param e 异常
     * @return YuanR
     */
    @ExceptionHandler(AuthenticationException.class)
    public YuanR<String> handleException(AuthenticationException e) {
        return YuanR.fail(FORBIDDEN, e.getMessage());
    }

    /**
     * 用户不存在异常捕获
     *
     * @param e 异常
     * @return YuanR
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public YuanR<String> handleException(UsernameNotFoundException e) {
        return YuanR.fail(USER_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(FeignException.class)
    public YuanR<String> handleFeignException(FeignException e) {
        return YuanR.fail(FEIGN_API_ERROR, e.getMessage());
    }

    /**
     * 全局异常兜底捕获
     *
     * @param e 运行时异常
     * @return YuanR
     */
    @ExceptionHandler(Exception.class)
    public YuanR<JSONObject> baseHandleException(Exception e) {
        return YuanR.fail(INTERNAL_SERVER_ERROR);
    }
}
