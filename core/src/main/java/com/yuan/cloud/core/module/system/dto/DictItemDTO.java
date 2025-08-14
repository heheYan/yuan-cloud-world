package com.yuan.cloud.core.module.system.dto;

import com.yuan.cloud.core.base.dto.AbstractBaseDTO;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.yuan.cloud.system.entity.DictItem}
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictItemDTO extends AbstractBaseDTO implements Serializable {
    @Size(max = 255)
    String name;
    @Size(max = 255)
    String code;
    Long parentId;

    Integer sorted;

    private DictItemDTO parent;
    private List<DictItemDTO> childrenList;
}