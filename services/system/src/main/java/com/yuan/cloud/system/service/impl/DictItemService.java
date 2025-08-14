package com.yuan.cloud.system.service.impl;

import com.yuan.cloud.core.base.service.impl.BaseService;
import com.yuan.cloud.system.entity.DictItem;
import com.yuan.cloud.system.query.DictItemQuery;
import com.yuan.cloud.system.repository.DictItemRepository;
import com.yuan.cloud.system.service.IDictItemService;
import org.springframework.stereotype.Service;

/**
 * Description
 *
 * @author Mr.Y
 * Created on 2025-08-10 20:58
 */
@Service
public class DictItemService extends BaseService<DictItem, DictItemQuery> implements IDictItemService {

    public DictItemService(DictItemRepository baseRepository) {
        super(baseRepository);
    }
}
