package com.yuan.cloud.core.base.generator;

import cn.hutool.core.util.IdUtil;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * Description 自定义ID生成器
 *
 * @author Mr.Y
 * Created on 2025-08-06 20:08
 */
public class SnowIdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        return IdUtil.getSnowflake().nextId();
    }
}
