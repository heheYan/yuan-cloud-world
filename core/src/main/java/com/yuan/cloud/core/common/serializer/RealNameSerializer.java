package com.yuan.cloud.core.common.serializer;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author Mr.Y
 * Created on 2024-10-21 14:28
 * @description 真实姓名序列化器
 */
public class RealNameSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 自定义脱敏逻辑
        if (value != null) {
            String masked = DesensitizedUtil.chineseName(value);
            gen.writeString(masked);
        } else {
            gen.writeString(value);
        }
    }
}
