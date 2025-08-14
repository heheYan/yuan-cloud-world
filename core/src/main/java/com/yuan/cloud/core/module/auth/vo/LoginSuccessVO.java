package com.yuan.cloud.core.module.auth.vo;

import lombok.Data;

/**
 * @author Mr.Y
 * Created on 2025-07-29 17:04
 * @description 登录成功返回vo
 */
@Data
public class LoginSuccessVO {
    private String accessToken;
    private String refreshToken;
    private String idToken;
    private String tokenType;
    private Long expiresIn;
    private String scope;
    private String username;
    private String userId;
    private String avatar;
    private String nickname;
    private String email;
    private String phone;
}
