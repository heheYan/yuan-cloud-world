package com.yuan.cloud.core.common.annotation;

import com.yuan.cloud.core.base.enums.QueryTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description 查询条件注解
 *
 * @author Mr.Y
 * Created on 2025-08-06 18:32
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "YaQueryType", description = "查询条件注解，定义多种查询类型，方便统一构造查询条件")
public @interface YaQueryType {
    // 查询类型
    @Schema(description = "查询类型", implementation = QueryTypeEnum.class)
    QueryTypeEnum type();
}
