package com.yuan.cloud.core.config.beanscan;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Description 基础Bean扫描配置
 *
 * @author Mr.Y
 * Created on 2025-08-06 15:41
 */
@Configuration
@ComponentScan(value = "com.yuan.cloud.core")
public class BeanConfigScanConfig {
}
