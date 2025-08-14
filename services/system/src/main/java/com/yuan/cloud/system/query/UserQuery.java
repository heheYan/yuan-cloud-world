package com.yuan.cloud.system.query;

import com.yuan.cloud.core.base.enums.QueryTypeEnum;
import com.yuan.cloud.core.base.query.AbstractBaseQuery;
import com.yuan.cloud.core.common.annotation.YaQueryType;
import lombok.Getter;
import lombok.Setter;

/**
 * Description 用户查询参数
 *
 * @author Mr.Y
 * Created on 2025-08-06 19:39
 */
@Getter
@Setter
public class UserQuery extends AbstractBaseQuery {

    @YaQueryType(type = QueryTypeEnum.EQUAL)
    private String username;

    @YaQueryType(type = QueryTypeEnum.LIKE)
    private String nickName;
}
