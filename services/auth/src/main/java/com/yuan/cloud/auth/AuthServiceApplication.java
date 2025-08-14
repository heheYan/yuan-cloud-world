package com.yuan.cloud.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Description 任务服务启动类
 *
 * @author Mr.Y
 * Created on 2025-08-11 16:00
 */
@EnableFeignClients(basePackages = "com.yuan.cloud.auth.client") // 启用Feign客户端
@EnableDiscoveryClient // 启用服务注册与发现
@SpringBootApplication
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
