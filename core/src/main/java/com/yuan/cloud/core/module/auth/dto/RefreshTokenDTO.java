package com.yuan.cloud.core.module.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Description 刷新token的请求参数
 *
 * @author Mr.Y
 * Created on 2025-08-13 18:53
 */
@Data
public class RefreshTokenDTO {
    @NotNull(message = "刷新token不能为空")
    private String refreshToken;
}
