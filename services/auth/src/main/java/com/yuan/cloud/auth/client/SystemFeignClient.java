package com.yuan.cloud.auth.client;

import com.yuan.cloud.auth.client.fallback.SystemFeignClientFallback;
import com.yuan.cloud.core.common.response.YuanR;
import com.yuan.cloud.core.config.openfeign.YaCustomFeignConfig;
import com.yuan.cloud.core.module.system.dto.UserDTO;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author YuAN
 * Created on 2025-04-30 16:52
 * @description
 */
@FeignClient(name = "system-service", configuration = YaCustomFeignConfig.class, fallback = SystemFeignClientFallback.class)
public interface SystemFeignClient {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/admin/user/findByUsername/{username}")
    YuanR<UserDTO> findByUsername(@NotEmpty @PathVariable("username") String username);

    @GetMapping("/admin/user/{id}")
    YuanR<UserDTO> findById(@Nonnull @PathVariable("id") String id);
}
