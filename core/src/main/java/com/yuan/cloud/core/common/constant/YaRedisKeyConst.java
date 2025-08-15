package com.yuan.cloud.core.common.constant;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Description redis缓存key相关常量类
 *
 * @author Mr.Y
 * Created on 2025-08-11 16:37
 */
@Schema(name = "YaRedisKeyConst", description = "redis缓存key相关常量类")
public class YaRedisKeyConst {

    @Schema(description = "服务端请求头中的traceId")
    public static final String SERVER_HEADER_TRACE_ID = "yuan:server:trace";

    @Schema(description = "用户登录token缓存key")
    public static final String YUAN_LOGIN_TOKEN_CACHE_KEY = "yuan:login:token:";

    @Schema(description = "验证码缓存key")
    public static final String CAPTCHA_KEY = "yuan:captcha:";

    /**
     * 验证码类型 -  登录
     *
     * @see com.yuan.cloud.core.common.enums.CaptchaTypeEnum
     */
    public static final String CAPTCHA_TYPE_LOGIN = "login";
    /**
     * 验证码类型 -  注册
     *
     * @see com.yuan.cloud.core.common.enums.CaptchaTypeEnum
     */
    public static final String CAPTCHA_TYPE_REGISTER = "register";
    /**
     * 验证码类型 -  重置密码
     *
     * @see com.yuan.cloud.core.common.enums.CaptchaTypeEnum
     */
    public static final String CAPTCHA_TYPE_RESET_PASSWORD = "resetPassword";
    /**
     * 验证码类型 -  修改密码
     *
     * @see com.yuan.cloud.core.common.enums.CaptchaTypeEnum
     */
    public static final String CAPTCHA_TYPE_CHANGE_PASSWORD = "changePassword";
    /**
     * 验证码类型 -  其他
     *
     * @see com.yuan.cloud.core.common.enums.CaptchaTypeEnum
     */
    public static final String CAPTCHA_TYPE_OTHER = "other";

}
