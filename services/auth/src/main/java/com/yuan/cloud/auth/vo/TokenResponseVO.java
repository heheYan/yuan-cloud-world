package com.yuan.cloud.auth.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * Description 刷新token接口返回对象
 *
 * @author Mr.Y
 * Created on 2025-08-14 09:18
 */
@Data
@Schema(name = "TokenResponseVO", description = "刷新token接口返回对象")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenResponseVO implements Serializable {

    @Schema(description = "访问令牌")
    private String accessToken;

    @Schema(description = "刷新令牌")
    private String refreshToken;

    @Schema(description = "id令牌")
    private String idToken;

    @Schema(description = "令牌类型")
    private String tokenType;

    @Schema(description = "令牌过期时间")
    private Long expiresIn;
}
