package com.yuan.cloud.core.config.openfeign;

import com.yuan.cloud.core.common.constant.YaCommonConst;
import com.yuan.cloud.core.common.constant.YaOauthConst;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Description openFeign客户端通用配置
 *
 * @author Mr.Y
 * Created on 2025-08-12 15:09UserFeignClient
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty( // 可通过配置控制是否启用
        name = "yuan.config.scan.enabled",
        havingValue = "true",
        matchIfMissing = true // 默认启用
)
public class YaCustomFeignConfig {

    /**
     * 客户端请求添加请求头
     * Ya-Trace-Id 和 Ya-Auth-User
     *
     * @return
     */
    @Bean
    public RequestInterceptor clientTokenInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // 添加到请求头
                requestTemplate.header(YaCommonConst.REQ_HEADER_TRACE_ID, request.getHeader(YaCommonConst.REQ_HEADER_TRACE_ID));
                requestTemplate.header(YaOauthConst.AUTH_HEADER_USER, request.getHeader(YaOauthConst.AUTH_HEADER_USER));
            }
        };
    }
}
