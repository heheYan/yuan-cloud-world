package com.yuan.cloud.auth.client.fallback;

import com.yuan.cloud.auth.client.SystemFeignClient;
import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import com.yuan.cloud.core.common.response.YuanR;
import com.yuan.cloud.core.module.system.dto.UserDTO;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

/**
 * Description
 *
 * @author Mr.Y
 * Created on 2025-08-13 17:46
 */
@Component
public class SystemFeignClientFallback implements SystemFeignClient {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public YuanR<UserDTO> findByUsername(String username) {
        return YuanR.fail(YuanStatusEnum.USER_NOT_FOUND);
    }

    @Override
    public YuanR<UserDTO> findById(@Nonnull String id) {
        return YuanR.fail(YuanStatusEnum.SERVICE_UNAVAILABLE);
    }
}
