package com.yuan.cloud.core.common.enums;

import lombok.Getter;

/**
 * @author Mr.Y
 * Created on 2024-12-17 17:24
 * @description 查询类型枚举
 */
@Getter
public enum QueryTypeEnum {
    EQUAL("=", "等于"),
    GREATER_THAN(">", "大于"),
    LESS_THAN("<", "小于"),
    GREATER_THAN_OR_EQUAL_TO(">=", "大于等于"),
    LESS_THAN_OR_EQUAL_TO("<=", "小于等于"),
    LIKE("like", "模糊查询"),
    NOT_EQUAL("!=", "不等于"),
    IN("in", "包含"),
    NOT_IN("not in", "不包含"),
    BETWEEN("between", "区间查询"),
    IS_NULL("is null", "为空"),
    IS_NOT_NULL("is not null", "不为空");
    private final String value;
    private final String desc;

    QueryTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
