package com.yuan.cloud.auth.keystore;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Mr.Y
 * Created on 2024-12-06 20:44
 * @description
 */
@Data
@Component
@ConfigurationProperties(prefix = "yuan.keystore")
public class KeystoreProperties {
    private String path;
    private String password;
    private String keyAlias;
    private String keyPassword;
}
