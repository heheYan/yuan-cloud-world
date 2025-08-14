package com.yuan.cloud.system.api;

import com.yuan.cloud.core.base.api.AbstractBaseApi;
import com.yuan.cloud.core.common.annotation.YaApi;
import com.yuan.cloud.core.module.system.dto.RouteDTO;
import com.yuan.cloud.core.module.system.vo.RouteVO;
import com.yuan.cloud.system.entity.Route;
import com.yuan.cloud.system.query.RouteQuery;
import com.yuan.cloud.system.service.IRouteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description 系统路由信息api接口
 *
 * @author Mr.Y
 * Created on 2025-08-10 21:20
 */
@YaApi
@RequestMapping("/route")
@Tag(name = "系统路由信息模块", description = "系统路由信息相关的增删改查接口")
public class RouteApi extends AbstractBaseApi<Route, RouteQuery, RouteDTO, RouteVO, IRouteService> {
    public RouteApi(IRouteService baseService) {
        super(baseService);
    }

    @Override
    protected Class<Route> getEntityClass() {
        return Route.class;
    }

    @Override
    protected Class<RouteVO> getVoClass() {
        return RouteVO.class;
    }
}
