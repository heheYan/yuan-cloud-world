package com.yuan.cloud.core.module.system.dto;

import com.yuan.cloud.core.base.dto.AbstractBaseDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link com.yuan.cloud.system.entity.Role}
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleDTO extends AbstractBaseDTO implements Serializable {
    @Size(max = 255)
    String name;
    @Size(max = 255)
    String code;
    @Size(max = 255)
    String description;
    Boolean enabled;
    List<RouteDTO> routes = new ArrayList<>();
    private List<UserDTO> users = new ArrayList<>();
}