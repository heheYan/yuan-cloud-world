package com.yuan.cloud.system.api;

import com.yuan.cloud.core.base.api.AbstractBaseApi;
import com.yuan.cloud.core.common.annotation.YaApi;
import com.yuan.cloud.core.module.system.dto.RoleDTO;
import com.yuan.cloud.core.module.system.vo.RoleVO;
import com.yuan.cloud.system.entity.Role;
import com.yuan.cloud.system.query.RoleQuery;
import com.yuan.cloud.system.service.IRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description 系统用户api接口
 *
 * @author Mr.Y
 * Created on 2025-08-10 21:06
 */
@YaApi
@RequestMapping("/role")
@Tag(name = "系统角色信息模块", description = "系统角色信息相关的增删改查接口")
public class RoleApi extends AbstractBaseApi<Role, RoleQuery, RoleDTO, RoleVO, IRoleService> {
    public RoleApi(IRoleService baseService) {
        super(baseService);
    }

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    @Override
    protected Class<RoleVO> getVoClass() {
        return RoleVO.class;
    }
}
