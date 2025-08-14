package com.yuan.cloud.system.service;

import com.yuan.cloud.core.base.service.IBaseService;
import com.yuan.cloud.system.entity.User;
import com.yuan.cloud.system.query.UserQuery;
import org.springframework.lang.NonNull;

/**
 * Description 用户信息接口
 *
 * @author Mr.Y
 * Created on 2025-08-06 19:38
 */
public interface IUserService extends IBaseService<User, UserQuery> {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User findByUsernameIgnoreCase(@NonNull String username);
}
