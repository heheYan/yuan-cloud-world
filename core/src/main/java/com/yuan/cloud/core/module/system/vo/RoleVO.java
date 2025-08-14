package com.yuan.cloud.core.module.system.vo;

import com.yuan.cloud.core.base.vo.AbstractBaseVO;
import com.yuan.cloud.core.module.system.dto.RouteDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Description
 *
 * @author Mr.Y
 * Created on 2025-08-10 21:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleVO extends AbstractBaseVO implements Serializable {
    @Size(max = 255)
    String name;
    @Size(max = 255)
    String code;
    @Size(max = 255)
    String description;
    Boolean enabled;
    List<RouteDTO> routes;
}
