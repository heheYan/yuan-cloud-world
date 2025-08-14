package com.yuan.cloud.auth.authentication.password;

import cn.hutool.core.util.StrUtil;
import com.yuan.cloud.core.common.constant.YaOauthConst;
import com.yuan.cloud.core.common.enums.CaptchaTypeEnum;
import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import com.yuan.cloud.core.common.exception.YuanApiException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YuAN
 * Created on 2025-05-12 18:08
 * @description 密码授权转换器
 */
@Component
@RequiredArgsConstructor
public class PasswordGrantAuthenticationConverter implements AuthenticationConverter {
    private final StringRedisTemplate redisTemplate;

    /**
     * 从request中提取请求参数，然后存入MultiValueMap<String, String>
     */
    private static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)  只处理 自定义密码模式授权
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!YaOauthConst.CUSTOM_GRANT_TYPE_PASSWORD.equals(grantType)) {
            return null;
        }
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        //从request中提取请求参数，然后存入MultiValueMap<String, String>
        MultiValueMap<String, String> parameters = getParameters(request);

        // username (REQUIRED)
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) ||
                parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            throw new YuanApiException(YuanStatusEnum.OAUTH2_USERNAME_NOT_EXISTS);
        }
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) ||
                parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            throw new YuanApiException(YuanStatusEnum.OAUTH2_PASSWORD_NOT_EXISTS);
        }
        // 读取 参数中传递的验证码id 和 验证码
        String captchaId = request.getParameter("captchaId");
        String actualCaptcha = request.getParameter("captchaCode");
        // 验证码参数校验
        if (StrUtil.isBlankIfStr(captchaId) || StrUtil.isBlankIfStr(actualCaptcha)) {
            throw new YuanApiException(YuanStatusEnum.CAPTCHA_CODE_MISSING);
        }
        // 获取缓存中的验证码
        String cacheCaptcha = redisTemplate.opsForValue().get(CaptchaTypeEnum.LOGIN.getType() + captchaId);

        // 如果验证码不存在，标识已过期 或 伪造请求，抛出异常
        if (StrUtil.isBlankIfStr(cacheCaptcha)) {
            throw new YuanApiException(YuanStatusEnum.CAPTCHA_INVALID);
        }
        // 验证码正确性校验
        if (!StrUtil.equals(cacheCaptcha, actualCaptcha)) {
            throw new YuanApiException(YuanStatusEnum.CAPTCHA_CODE_NOT_EQUALS);
        }

        // 验证码正确, 移除缓存的验证码信息，继续后续过滤器链
        redisTemplate.delete(CaptchaTypeEnum.LOGIN.getType() + captchaId);

        // 收集要传入PasswordGrantAuthenticationToken构造方法的参数，
        // 该参数接下来在PasswordGrantAuthenticationProvider中使用
        Map<String, Object> additionalParameters = new HashMap<>();
        // 遍历从request中提取的参数，排除掉grant_type、client_id、code等字段参数，其他参数收集到additionalParameters中
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CODE)) {
                additionalParameters.put(key, value.getFirst());
            }
        });

        //返回自定义的PasswordGrantAuthenticationToken对象
        return new PasswordGrantAuthenticationToken(clientPrincipal, additionalParameters);
    }
}
