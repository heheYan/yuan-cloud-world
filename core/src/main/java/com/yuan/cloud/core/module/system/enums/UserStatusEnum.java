package com.yuan.cloud.core.module.system.enums;

import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import com.yuan.cloud.core.common.exception.YuanApiException;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Description 用户状态枚举
 *
 * @author Mr.Y
 * Created on 2025-08-14 10:40
 */
@Getter
public enum UserStatusEnum {

    NORMAL("1", "正常"),
    DISABLE("0", "禁用");

    private final String code;
    private final String desc;

    UserStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 缓存code到枚举的映射，提高性能
    private static final Map<String, UserStatusEnum> CODE_MAP = new HashMap<>();

    static {
        for (UserStatusEnum status : UserStatusEnum.values()) {
            CODE_MAP.put(status.getCode(), status);
        }
    }

    /**
     * 判断code是否有效
     *
     * @param code 状态码
     * @return 是否有效
     */
    public static boolean isValidCode(String code) {
        return CODE_MAP.containsKey(code);
    }

    /**
     * 根据code获取枚举值
     *
     * @param code 状态码
     * @return 对应的枚举值，如果不存在返回null
     */
    public static UserStatusEnum fromCode(String code) {
        return CODE_MAP.get(code);
    }

    /**
     * 根据code获取枚举值（Optional版本）
     *
     * @param code 状态码
     * @return 对应的枚举值Optional
     */
    public static Optional<UserStatusEnum> fromCodeOptional(String code) {
        return Optional.ofNullable(CODE_MAP.get(code));
    }

    /**
     * 根据code获取枚举值，如果不存在则抛出异常
     *
     * @param code 状态码
     * @return 对应的枚举值
     * @throws YuanApiException 如果code无效
     */
    public static UserStatusEnum fromCodeOrThrow(String code) {
        UserStatusEnum status = CODE_MAP.get(code);
        if (status == null) {
            throw new YuanApiException(YuanStatusEnum.REQUEST_PARAM_VALID_ERROR.getMsg() + ":" + code);
        }
        return status;
    }
}
