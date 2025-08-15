package com.yuan.cloud.system.query;

import com.yuan.cloud.core.base.enums.QueryTypeEnum;
import com.yuan.cloud.core.base.query.AbstractBaseQuery;
import com.yuan.cloud.core.common.annotation.YaQueryType;
import lombok.Getter;
import lombok.Setter;

/**
 * Description 角色信息查询实体对象
 *
 * @author Mr.Y
 * Created on 2025-08-10 20:54
 */
@Setter
@Getter
public class RoleQuery extends AbstractBaseQuery {

    @YaQueryType(type = QueryTypeEnum.EQUAL)
    private String isEnabled;
}
