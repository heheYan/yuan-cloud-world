package com.yuan.cloud.system.service.impl;

import com.yuan.cloud.core.base.service.impl.BaseService;
import com.yuan.cloud.system.entity.Role;
import com.yuan.cloud.system.query.RoleQuery;
import com.yuan.cloud.system.repository.RoleRepository;
import com.yuan.cloud.system.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * Description
 *
 * @author Mr.Y
 * Created on 2025-08-10 21:04
 */
@Service
public class RoleService extends BaseService<Role, RoleQuery> implements IRoleService {
    public RoleService(RoleRepository baseRepository) {
        super(baseRepository);
    }
}
