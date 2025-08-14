package com.yuan.cloud.system.repository;

import com.yuan.cloud.core.base.repository.BaseRepository;
import com.yuan.cloud.system.entity.User;
import org.springframework.lang.NonNull;

public interface UserRepository extends BaseRepository<User> {
    User findByUsernameIgnoreCase(@NonNull String username);

}