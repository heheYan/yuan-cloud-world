package com.yuan.cloud.core.module.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Mr.Y
 * Created on 2025-07-29 16:20
 * @description 用户登录表单DTO
 */
@Data
public class UserLoginDTO {

    @NotNull(message = "用户名不能为空")
    private String username;
    @NotNull(message = "密码不能为空")
    private String password;

    @NotNull(message = "验证码不能为空")
    private String code;
    @NotNull(message = "验证码ID不能为空")
    private String captchaId;

}
