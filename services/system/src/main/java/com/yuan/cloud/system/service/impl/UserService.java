package com.yuan.cloud.system.service.impl;

import com.yuan.cloud.core.base.service.impl.BaseService;
import com.yuan.cloud.system.entity.User;
import com.yuan.cloud.system.query.UserQuery;
import com.yuan.cloud.system.repository.UserRepository;
import com.yuan.cloud.system.service.IUserService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * Description
 *
 * @author Mr.Y
 * Created on 2025-08-06 19:40
 */
@Service
public class UserService extends BaseService<User, UserQuery> implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository baseRepository) {
        super(baseRepository);
        this.userRepository = baseRepository;
    }

    @Override
    public User findByUsernameIgnoreCase(@NonNull String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

}
