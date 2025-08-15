package com.yuan.cloud.core.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.yuan.cloud.core.common.constant.YaRedisKeyConst.*;

/**
 * @author Mr.Y
 * Created on 2025-05-20 16:27
 * @description 验证码类型枚举
 */
@Getter
public enum CaptchaTypeEnum {

    LOGIN(CAPTCHA_TYPE_LOGIN, "登录验证码", 2),
    REGISTER(CAPTCHA_TYPE_REGISTER, "注册验证码", 30 * 60),
    RESET_PASSWORD(CAPTCHA_TYPE_RESET_PASSWORD, "重置密码验证码", 5),
    CHANGE_PASSWORD(CAPTCHA_TYPE_CHANGE_PASSWORD, "修改密码验证码", 5),
    OTHER(CAPTCHA_TYPE_OTHER, "其他验证码", 5);

    /**
     * 类型
     */
    private final String type;
    /**
     * 描述
     */
    private final String desc;
    /**
     * 缓存时间， 分钟
     */
    private final int cacheTime;

    CaptchaTypeEnum(String type, String desc, int cacheTime) {
        this.type = type;
        this.desc = desc;
        this.cacheTime = cacheTime;
    }

    // 缓存code到枚举的映射，提高性能
    private static final Map<String, CaptchaTypeEnum> TYPE_MAP = new HashMap<>();

    static {
        for (CaptchaTypeEnum typeEnum : CaptchaTypeEnum.values()) {
            TYPE_MAP.put(typeEnum.getType(), typeEnum);
        }
    }

    /**
     * 判断 type 是否有效
     *
     * @param type 状态码
     * @return 是否有效
     */
    public static boolean isValidType(String type) {
        return TYPE_MAP.containsKey(type);
    }

    /**
     * 根据类型获取枚举
     *
     * @param type 类型
     * @return 枚举
     */
    public static CaptchaTypeEnum getByType(String type) {
        return TYPE_MAP.get(type);
    }
}
