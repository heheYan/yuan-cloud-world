package com.yuan.cloud.core.base.query.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.yuan.cloud.core.base.entity.AbstractBaseEntity;
import com.yuan.cloud.core.base.enums.QueryTypeEnum;
import com.yuan.cloud.core.base.query.AbstractBaseQuery;
import com.yuan.cloud.core.common.annotation.YaQueryType;
import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import com.yuan.cloud.core.common.exception.YuanApiException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * Description 通用查询条件生成工具类
 *
 * @author Mr.Y
 * Created on 2025-08-06 18:14
 */
@Slf4j
@Tag(name = "QueryBuilderUtil", description = "查询条件工具类")
public class QueryBuilderUtil {

    /**
     * 构建查询条件
     *
     * @param condition 查询条件
     * @return Specification
     */
    public static <T extends AbstractBaseEntity, Q extends AbstractBaseQuery> Specification<T> builder(Q condition) {
        Specification<T> spec = Specification.where((root, query, criteriaBuilder) -> null);

        if (condition != null) {
            Field[] fields = condition.getClass().getDeclaredFields();
            for (Field field : fields) {
                // 确保可以访问字段
                ReflectionUtils.makeAccessible(field);
                // 获取字段的查询类型
                YaQueryType queryType = field.getAnnotation(YaQueryType.class);
                // 如果字段没有查询类型注解，则跳过
                if (queryType == null) {
                    continue;
                }
                QueryTypeEnum type = queryType.type();
                Object fieldValue = null;
                try {
                    fieldValue = field.get(condition);
                } catch (IllegalAccessException e) {
                    log.error(ExceptionUtil.stacktraceToString(e));
                    throw new YuanApiException(YuanStatusEnum.QUERY_CONDITION_ERROR);
                }
                if (fieldValue == null || StrUtil.isBlankIfStr(fieldValue)) {
                    continue;
                }

                Object finalValue = fieldValue;
                spec = spec.and((root, query, cb) -> {
                    switch (type) {
                        // 等于
                        case EQUAL -> {
                            return cb.equal(root.get(field.getName()), finalValue);
                        }
                        // 模糊查询
                        case LIKE -> {
                            return cb.like(root.get(field.getName()), "%" + finalValue + "%");
                        }
                        // 包含
                        case IN -> {
                            return cb.in(root.get(field.getName())).in(finalValue);
                        }
                        case NOT_EQUAL -> {
                            return cb.notEqual(root.get(field.getName()), finalValue);
                        }
                        default -> {
                            return cb.conjunction();
                        }
                    }
                });
            }
        }
        return spec;
    }

}