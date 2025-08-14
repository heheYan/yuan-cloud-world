package com.yuan.cloud.system.service.impl;

import com.yuan.cloud.core.base.service.impl.BaseService;
import com.yuan.cloud.system.entity.Route;
import com.yuan.cloud.system.query.RouteQuery;
import com.yuan.cloud.system.repository.RouteRepository;
import com.yuan.cloud.system.service.IRouteService;
import org.springframework.stereotype.Service;

/**
 * Description
 *
 * @author Mr.Y
 * Created on 2025-08-10 21:05
 */
@Service
public class RouteService extends BaseService<Route, RouteQuery> implements IRouteService {
    public RouteService(RouteRepository baseRepository) {
        super(baseRepository);
    }
}
