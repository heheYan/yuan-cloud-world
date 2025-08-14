package com.yuan.cloud.core.common.enums;

import lombok.Getter;

/**
 * @author Mr.Y
 * Created on 2024-12-11 11:25
 * @description 附件标签枚举
 */
@Getter
public enum YuanAttachTagEnum {

    USER_AVATAR("avatar", "用户头像"),
    CARD_BACKGROUND("card_background", "卡牌背景"),
    CARD_FRONT("card_front", "卡牌正面");

    private final String code;
    private final String name;

    YuanAttachTagEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
