package com.yuan.cloud.core.common.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description 白名单配置文件
 *
 * @author Mr.Y
 * Created on 2025-08-11 16:48
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "yuan.ignored")
public class IgnoredUrlProperties {
    private String[] urls;
}
