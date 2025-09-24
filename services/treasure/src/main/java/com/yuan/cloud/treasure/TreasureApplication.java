package com.yuan.cloud.treasure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Description 寻宝游戏服务启动类
 *
 * @author Mr.Y
 * Created on 2025-09-08 15:06
 */
@EnableDiscoveryClient
@SpringBootApplication
public class TreasureApplication {
    public static void main(String[] args) {
        SpringApplication.run(TreasureApplication.class, args);
    }
}
