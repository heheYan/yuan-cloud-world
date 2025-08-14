package com.yuan.cloud.core.common.util;

import com.alibaba.fastjson2.JSONObject;
import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import com.yuan.cloud.core.common.exception.YuanApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author YuAN
 * Created on 2025-05-08 22:36
 * description 认证相关工具类
 */
@Slf4j
public class YaAuthUtil {
    /**
     * 私有构造方法
     */
    private YaAuthUtil() {
    }

    /**
     * 认证与鉴权失败回调
     *
     * @param request  当前请求
     * @param response 当前响应
     * @param e        具体的异常信息
     */
    public static void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        Map<String, Object> parameters = getErrorParameter(request, response, e);
        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSONObject.from(parameters).toJSONString());
            response.getWriter().flush();
        } catch (IOException ex) {
            log.error("写回错误信息失败", e);
        }
    }

    /**
     * 获取异常信息map
     *
     * @param request  当前请求
     * @param response 当前响应
     * @param e        本次异常具体的异常实例
     * @return 异常信息map
     */
    private static Map<String, Object> getErrorParameter(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        Map<String, Object> parameters = new LinkedHashMap<>();
        if (request.getUserPrincipal() instanceof AbstractOAuth2TokenAuthenticationToken) {
            // 权限不足
            parameters.put("error", BearerTokenErrorCodes.INSUFFICIENT_SCOPE);
            parameters.put("code", YuanStatusEnum.FORBIDDEN.getCode());
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
        if (e instanceof OAuth2AuthenticationException authenticationException) {
            // jwt异常，e.g. jwt超过有效期、jwt无效等
            OAuth2Error error = authenticationException.getError();
            parameters.put("error", error.getErrorCode());
            parameters.put("code", YuanStatusEnum.TOKEN_INVALID.getCode());
            if (StringUtils.hasText(error.getUri())) {
                parameters.put("error_uri", error.getUri());
            }
            if (StringUtils.hasText(error.getDescription())) {
                parameters.put("error_description", error.getDescription());
            }
            if (error instanceof BearerTokenError bearerTokenError) {
                if (StringUtils.hasText(bearerTokenError.getScope())) {
                    parameters.put("scope", bearerTokenError.getScope());
                }
                response.setStatus(bearerTokenError.getHttpStatus().value());
            }
        }
        if (e instanceof InsufficientAuthenticationException) {
            // 没有携带jwt访问接口，没有客户端认证信息
            parameters.put("error", BearerTokenErrorCodes.INVALID_TOKEN);
            parameters.put("code", YuanStatusEnum.UNAUTHORIZED.getCode());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        if (e instanceof YuanApiException) {
            parameters.put("code", ((YuanApiException) e).getErrEnum().getCode());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        parameters.put("msg", e.getMessage());
        parameters.put("timestamp", System.currentTimeMillis());
        parameters.put("data", e.getLocalizedMessage());
        parameters.put("success", false);
        return parameters;
    }

}
