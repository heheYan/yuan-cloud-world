package com.yuan.cloud.auth.api;

import com.yuan.cloud.auth.service.LoginService;
import com.yuan.cloud.auth.util.YaCaptchaUtil;
import com.yuan.cloud.auth.vo.TokenResponseVO;
import com.yuan.cloud.auth.vo.YaCaptchaVO;
import com.yuan.cloud.core.common.annotation.YaApi;
import com.yuan.cloud.core.common.enums.CaptchaTypeEnum;
import com.yuan.cloud.core.common.response.YuanR;
import com.yuan.cloud.core.module.auth.dto.RefreshTokenDTO;
import com.yuan.cloud.core.module.auth.dto.UserLoginDTO;
import com.yuan.cloud.core.module.auth.vo.LoginSuccessVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.TimeUnit;

/**
 * Description 认证相关接口
 *
 * @author Mr.Y
 * Created on 2025-08-11 18:19
 */
@Slf4j
@YaApi
@Tag(name = "认证模块", description = "认证相关接口")
@RequiredArgsConstructor
public class AuthApi {
    private final LoginService loginService;
    private final StringRedisTemplate redisTemplate;

    @Operation(summary = "获取验证码", responses = {@ApiResponse(responseCode = "200", description = "获取验证码成功",
            content = @Content(schema = @Schema(implementation = YaCaptchaVO.class)))})
    @GetMapping("/captcha")
    public YaCaptchaVO captcha() {
        // 生成验证码
        YaCaptchaVO yaCaptchaVO = YaCaptchaUtil.buildCaptchaVO();
        // 缓存验证码，2分钟过期
        redisTemplate.opsForValue().set(CaptchaTypeEnum.LOGIN.getType() + yaCaptchaVO.getCaptchaId(),
                yaCaptchaVO.getCaptchaCode(), CaptchaTypeEnum.LOGIN.getCacheTime(), TimeUnit.MINUTES);
        // 返回验证码
        return yaCaptchaVO;
    }

    @Operation(description = "用户登录", parameters = {@Parameter(name = "userLoginDTO", schema = @Schema(implementation = UserLoginDTO.class), description = "用户登录信息")},
            responses = {@ApiResponse(description = "登录成功", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = LoginSuccessVO.class)))
            })
    @PostMapping("/login")
    public LoginSuccessVO login(@Validated @RequestBody UserLoginDTO dto) {
        return loginService.login(dto);
    }

    @Operation(summary = "刷新token", parameters = {@Parameter(name = "refreshTokenDTO", schema = @Schema(implementation = RefreshTokenDTO.class), description = "刷新token信息")},
            responses = {@ApiResponse(description = "刷新成功", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TokenResponseVO.class)))
            })
    @PostMapping("/refreshToken")
    public YuanR<TokenResponseVO> refreshToken(@Validated @RequestBody RefreshTokenDTO dto) {
        return loginService.refreshToken(dto);
    }
}
