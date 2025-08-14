package com.yuan.cloud.core.module.system.dto;

import com.yuan.cloud.core.base.dto.AbstractBaseDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * DTO for {@link com.yuan.cloud.system.entity.Route}
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RouteDTO extends AbstractBaseDTO implements Serializable {
    @Size(max = 255)
    String name;
    @Size(max = 255)
    String code;
    @Size(max = 4)
    String type;
    @Size(max = 255)
    String path;
    @Size(max = 255)
    String icon;
    Long pId;
    @Size(max = 255)
    String pCode;
    Boolean isEnabled;
}