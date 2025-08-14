package com.yuan.cloud.core.module.system.vo;

import com.yuan.cloud.core.base.vo.AbstractBaseVO;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description
 *
 * @author Mr.Y
 * Created on 2025-08-10 21:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RouteVO extends AbstractBaseVO implements Serializable {
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
