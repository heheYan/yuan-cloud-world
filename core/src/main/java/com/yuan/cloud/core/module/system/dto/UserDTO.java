package com.yuan.cloud.core.module.system.dto;

import com.yuan.cloud.core.base.dto.AbstractBaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.yuan.cloud.system.entity.User}
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends AbstractBaseDTO implements Serializable {
    String username;
    String pwd;
    String nickName;
    String avatar;
    String email;
    String mobile;
    String openId;
    String status;
    List<RoleDTO> roles;
}