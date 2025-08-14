package com.yuan.cloud.auth.service;

import cn.hutool.core.util.StrUtil;
import com.yuan.cloud.auth.authentication.password.PasswordGrantAuthenticationToken;
import com.yuan.cloud.auth.util.YaTokenUtil;
import com.yuan.cloud.auth.vo.TokenResponseVO;
import com.yuan.cloud.core.common.constant.YaCommonConst;
import com.yuan.cloud.core.common.constant.YaOauthConst;
import com.yuan.cloud.core.common.enums.CaptchaTypeEnum;
import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import com.yuan.cloud.core.common.exception.YuanApiException;
import com.yuan.cloud.core.common.response.YuanR;
import com.yuan.cloud.core.module.auth.dto.RefreshTokenDTO;
import com.yuan.cloud.core.module.auth.dto.UserLoginDTO;
import com.yuan.cloud.core.module.auth.vo.LoginSuccessVO;
import com.yuan.cloud.core.module.system.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

/**
 * Description 登录实现方法
 *
 * @author Mr.Y
 * Created on 2025-08-12 17:09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
    private final WebClient.Builder webClientBuilder;
    private final AuthenticationManager authenticationManager;
    private final RegisteredClientRepository registeredClientRepository;
    private final AuthorizationServerSettings authorizationServerSettings;
    private final YaTokenUtil yaTokenUtil;
    private final StringRedisTemplate redisTemplate;

    // region 密码模式实现
    public LoginSuccessVO login(UserLoginDTO userLoginDTO) {
        try {
            // 手动设置 AuthorizationServerContext，确保在 Service 层调用时也有上下文
            initialContext();
            // 获取注册客户端
            RegisteredClient client = registeredClientRepository.findByClientId(YaOauthConst.AUTH_CLIENT_ID);
            if (client == null) {
                throw new YuanApiException(YuanStatusEnum.INVALID_CLIENT);
            }

            // 校验验证码信息
            String cacheCaptcha = redisTemplate.opsForValue().get(CaptchaTypeEnum.LOGIN.getType() + userLoginDTO.getCaptchaId());
            // 如果缓存中不存在，标识已过期 或 伪造请求，抛出异常
            if (StrUtil.isBlankIfStr(cacheCaptcha)) {
                throw new YuanApiException(YuanStatusEnum.CAPTCHA_INVALID);
            }
            // 输入的验证码与缓存中的验证码不一致
            if (!userLoginDTO.getCode().equals(cacheCaptcha)) {
                throw new YuanApiException(YuanStatusEnum.CAPTCHA_CODE_NOT_EQUALS);
            }
            // 校验一致，删除缓存的验证码
            redisTemplate.delete(CaptchaTypeEnum.LOGIN.getType() + userLoginDTO.getCaptchaId());

            // 构建 PasswordGrantAuthenticationToken
            PasswordGrantAuthenticationToken passwordGrantAuthenticationToken = getPasswordGrantAuthenticationToken(userLoginDTO, client);

            // 执行认证，走 ya_password 模式
            Authentication authenticate = authenticationManager.authenticate(passwordGrantAuthenticationToken);

            // 认证成功，封装成 LoginSuccessVO
            if (authenticate instanceof OAuth2AccessTokenAuthenticationToken authenticationToken) {
                LoginSuccessVO loginSuccessVO = new LoginSuccessVO();
                OAuth2AccessToken accessToken = authenticationToken.getAccessToken();
                String token = accessToken.getTokenValue();
                loginSuccessVO.setAccessToken(token);
                loginSuccessVO.setRefreshToken(authenticationToken.getRefreshToken().getTokenValue());
                loginSuccessVO.setTokenType(accessToken.getTokenType().getValue());
                loginSuccessVO.setExpiresIn(accessToken.getExpiresAt().getEpochSecond() -
                        Instant.now().getEpochSecond());
                loginSuccessVO.setScope(accessToken.getScopes().toString());
                // 添加到缓存
                yaTokenUtil.cacheToken(token);
                // 查询登录用户信息
                UserDTO dto = yaTokenUtil.getUser(token);
                loginSuccessVO.setUsername(dto.getUsername());
                loginSuccessVO.setUserId(dto.getId().toString());
                loginSuccessVO.setAvatar(dto.getAvatar());
                loginSuccessVO.setNickname(dto.getNickName());
                loginSuccessVO.setEmail(dto.getEmail());
                loginSuccessVO.setPhone(dto.getMobile());
                return loginSuccessVO;
            }
            // 认证失败，抛出异常信息，提示 accessToken 生成失败
            throw new YuanApiException(YuanStatusEnum.ASSESS_TOKEN_GENERATE_ERROR);
        } finally {
            // 清理上下文，避免内存泄漏
            AuthorizationServerContextHolder.resetContext();
        }
    }

    private void initialContext() {
        // 手动设置 AuthorizationServerContext，确保在 Service 层调用时也有上下文
        AuthorizationServerContext context = AuthorizationServerContextHolder.getContext();
        if (context == null) {
            // 创建一个默认的上下文
            context = new AuthorizationServerContext() {
                @Override
                public String getIssuer() {
                    return authorizationServerSettings.getIssuer();
                }

                @Override
                public AuthorizationServerSettings getAuthorizationServerSettings() {
                    return authorizationServerSettings;
                }
            };
            AuthorizationServerContextHolder.resetContext();
            AuthorizationServerContextHolder.setContext(context);
        }
    }

    private static PasswordGrantAuthenticationToken getPasswordGrantAuthenticationToken(UserLoginDTO userLoginDTO, RegisteredClient client) {
        OAuth2ClientAuthenticationToken clientAuthenticationToken = new OAuth2ClientAuthenticationToken(client,
                ClientAuthenticationMethod.CLIENT_SECRET_BASIC, client.getClientSecret());

        Map<String, Object> additionalParameters = Map.of(
                "username", userLoginDTO.getUsername(),
                "password", userLoginDTO.getPassword(),
                "captchaId", userLoginDTO.getCaptchaId(),
                "captchaCode", userLoginDTO.getCode()
        );
        return new PasswordGrantAuthenticationToken(clientAuthenticationToken, additionalParameters);
    }
    // endregion

    /**
     * 刷新 token
     *
     * @param dto 刷新token参数
     * @return 刷新后的token
     */
    public YuanR<TokenResponseVO> refreshToken(RefreshTokenDTO dto) {
        WebClient webClient = webClientBuilder.build();

        String clientCredentials = YaOauthConst.AUTH_CLIENT_ID + ":" + YaOauthConst.AUTH_CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(clientCredentials.getBytes());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", dto.getRefreshToken());

        return webClient.post()
                .uri("lb://auth-service" + authorizationServerSettings.getTokenEndpoint())
                .headers(httpHeaders -> {
                    httpHeaders.add(YaCommonConst.AUTHORIZATION_HEADER, "Basic " + encodedCredentials);
                    httpHeaders.add(YaCommonConst.REQ_HEADER_TRACE_ID, yaTokenUtil.getCurrentRequestTraceId());
                })
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<YuanR<TokenResponseVO>>() {
                })
                .doOnError(e -> {
                    throw new YuanApiException(e.getMessage());
                })
                .block();
    }

    // region http请求调用密码模式获取access_token
    public LoginSuccessVO obtainAccessToken(UserLoginDTO userLoginDTO) {
        WebClient webClient = webClientBuilder.build();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", YaOauthConst.CUSTOM_GRANT_TYPE_PASSWORD);
        formData.add("username", userLoginDTO.getUsername());
        formData.add("password", userLoginDTO.getPassword());
        formData.add("captchaId", userLoginDTO.getCaptchaId());
        formData.add("captchaCode", userLoginDTO.getCode());

        String clientCredentials = YaOauthConst.AUTH_CLIENT_ID + ":" + YaOauthConst.AUTH_CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(clientCredentials.getBytes());
        ResponseEntity<LoginSuccessVO> response = webClient.post()
                .uri("lb://auth-service" + authorizationServerSettings.getTokenEndpoint())
                .header("Authorization", "Basic " + encodedCredentials)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .toEntity(LoginSuccessVO.class)
                .block();

        assert response != null;
        return response.getBody();
    }
    // endregion

}