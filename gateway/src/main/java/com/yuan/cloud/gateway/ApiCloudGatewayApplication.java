package com.yuan.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Description 接口服务网关启动类
 *
 * @author Mr.Y
 * Created on 2025-08-11 15:51
 */
@EnableDiscoveryClient // 启用服务注册与发现
@SpringBootApplication(exclude = {AopAutoConfiguration.class})
public class ApiCloudGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiCloudGatewayApplication.class, args);
    }
}
