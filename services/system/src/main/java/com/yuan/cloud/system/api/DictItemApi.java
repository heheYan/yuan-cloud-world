package com.yuan.cloud.system.api;

import com.yuan.cloud.core.base.api.AbstractBaseApi;
import com.yuan.cloud.core.common.annotation.YaApi;
import com.yuan.cloud.core.module.system.dto.DictItemDTO;
import com.yuan.cloud.core.module.system.vo.DictItemVO;
import com.yuan.cloud.system.entity.DictItem;
import com.yuan.cloud.system.query.DictItemQuery;
import com.yuan.cloud.system.service.IDictItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description  系统数据字典api接口
 *
 * @author Mr.Y
 * Created on 2025-08-10 21:19
 */
@YaApi
@RequestMapping("/dictItem")
@Tag(name = "系统数据字典模块", description = "系统数据字典相关的增删改查接口")
public class DictItemApi extends AbstractBaseApi<DictItem, DictItemQuery, DictItemDTO, DictItemVO, IDictItemService> {
    public DictItemApi(IDictItemService baseService) {
        super(baseService);
    }

    @Override
    protected Class<DictItem> getEntityClass() {
        return DictItem.class;
    }

    @Override
    protected Class<DictItemVO> getVoClass() {
        return DictItemVO.class;
    }
}
