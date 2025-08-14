package com.yuan.cloud.core.config.beanscan;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Description 一些web工程需要的配置项
 *
 * @author Mr.Y
 * Created on 2025-08-12 10:49
 */
@Configuration
@EnableJpaAuditing  // 启用 JPA 审计功能
@ConditionalOnWebApplication
@ConditionalOnProperty( // 可通过配置控制是否启用
        name = "yuan.config.scan.enabled",
        havingValue = "true",
        matchIfMissing = true // 默认启用
)
public class CustomWebMvcConfig {
}
