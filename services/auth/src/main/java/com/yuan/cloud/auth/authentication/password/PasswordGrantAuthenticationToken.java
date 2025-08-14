package com.yuan.cloud.auth.authentication.password;

import com.yuan.cloud.core.common.constant.YaOauthConst;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

/**
 * @author YuAN
 * Created on 2025-05-12 17:57
 * @description 密码模式
 */
public class PasswordGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    /**
     * Sub-class constructor.
     *
     * @param clientPrincipal      the authenticated client principal
     * @param additionalParameters the additional parameters
     */
    public PasswordGrantAuthenticationToken(Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(YaOauthConst.CUSTOM_GRANT_TYPE_PASSWORD), clientPrincipal, additionalParameters);
    }
}
