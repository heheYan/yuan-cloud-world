package com.yuan.cloud.core.config.apiprefix;

import com.yuan.cloud.core.common.annotation.YaApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Mr.Y
 * Created on 2025-05-20 22:56
 * description  api 前缀配置
 */
@Configuration
@ConditionalOnProperty(prefix = "yuan.api", name = "prefix")
public class YaApiPrefixConfig implements WebMvcConfigurer {

    @Value("${yuan.api.prefix}")
    private String apiPrefix = "api";

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer
                .addPathPrefix(apiPrefix, c -> c.isAnnotationPresent(YaApi.class));
    }
}
