package com.yuan.cloud.core.module.system.vo;

import com.yuan.cloud.core.base.vo.AbstractBaseVO;
import com.yuan.cloud.core.module.system.dto.DictItemDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Description
 *
 * @author Mr.Y
 * Created on 2025-08-10 21:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictItemVO extends AbstractBaseVO implements Serializable {
    @Size(max = 255)
    String name;
    @Size(max = 255)
    String code;
    Long parentId;
    Integer sorted;
    private DictItemDTO parent;
    private List<DictItemDTO> childrenList;
}
