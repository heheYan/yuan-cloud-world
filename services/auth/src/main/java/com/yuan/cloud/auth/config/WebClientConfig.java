package com.yuan.cloud.auth.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Description WebClient 负载配置
 *
 * @author Mr.Y
 * Created on 2025-08-12 18:21
 */
@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced // 核心！让 WebClient 支持 lb:// 协议
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}
