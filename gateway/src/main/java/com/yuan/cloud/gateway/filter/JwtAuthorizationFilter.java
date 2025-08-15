package com.yuan.cloud.gateway.filter;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.yuan.cloud.core.common.constant.YaCommonConst;
import com.yuan.cloud.core.common.constant.YaOauthConst;
import com.yuan.cloud.core.common.constant.YaRedisKeyConst;
import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import com.yuan.cloud.core.common.property.IgnoredUrlProperties;
import com.yuan.cloud.core.common.response.YuanR;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Description 接口统一鉴权拦截器
 *
 * @author Mr.Y
 * Created on 2025-08-11 16:44
 */
@Slf4j
@Order(-1) // 优先级，数字越小越优先执行
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter implements GlobalFilter {
    @Schema(description = "白名单接口")
    private final IgnoredUrlProperties ignoredUrlProperties;

    @Schema(description = "Redis模板")
    private final RedisTemplate<String, Object> redisTemplate;

    private final JwtDecoder jwtDecoder;

    @Operation(description = "处理未授权方法")
    private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        YuanR<Object> fail = YuanR.fail(YuanStatusEnum.UNAUTHORIZED);
        byte[] bits = JSONObject.from(fail).toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);

        // 异常处理时增加删除traceId的逻辑
        ServerHttpRequest request = exchange.getRequest();
        String traceId = request.getHeaders().getFirst(YaCommonConst.REQ_HEADER_TRACE_ID);
        if (StrUtil.isNotBlank(traceId)) {
            redisTemplate.opsForSet().remove(YaRedisKeyConst.SERVER_HEADER_TRACE_ID, traceId);
        }
        return response.writeWith(Mono.just(buffer));
    }

    @Operation(description = "网关JWT认证全局过滤器")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String traceId = IdUtil.nanoId();
        request.mutate().header(YaCommonConst.REQ_HEADER_TRACE_ID, traceId);
        redisTemplate.opsForSet().add(YaRedisKeyConst.SERVER_HEADER_TRACE_ID, traceId);
        // 当前是否匹配白名单接口
        if (isExcludePath(String.valueOf(request.getPath()))) {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // 删除redis缓存的traceId
                redisTemplate.opsForSet().remove(YaRedisKeyConst.SERVER_HEADER_TRACE_ID, traceId);
            }));
        }
        // 0. 获取token信息
        String token = request.getHeaders().getFirst(YaCommonConst.AUTHORIZATION_HEADER);
        // 1. 如果token为空，直接返回报错信息
        if (StrUtil.isBlank(token)) {
            return handleUnauthorized(exchange);
        }
        // 2. 去掉Bearer前缀
        token = token.replace(YaCommonConst.BEARER_TOKEN_TYPE, "");
        // 3. 校验token，如果不满足条件，封装返回
        Jwt jwt = jwtDecoder.decode(token);
        if (jwt == null) {
            return handleUnauthorized(exchange);
        }
        // 4. token是否主动退出过
        // 4.1 判断redis已登录缓存中是否有记录
        if (StrUtil.isBlankIfStr(redisTemplate.opsForValue().get(YaRedisKeyConst.YUAN_LOGIN_TOKEN_CACHE_KEY + jwt.getId()))) {
            return handleUnauthorized(exchange);
        }
        // 4.2 请求头中添加用户信息
        request.mutate().header(YaOauthConst.AUTH_HEADER_USER, jwt.getClaims().get(YaOauthConst.JWT_CLAIM_USER_ID).toString());

        // 5. 放行
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            // 删除请求头
            redisTemplate.opsForSet().remove(YaRedisKeyConst.SERVER_HEADER_TRACE_ID, traceId);
        }));
    }

    @Operation(description = "判断是否是白名单接口")
    private boolean isExcludePath(String path) {
        // 未配置白名单，无需判断
        if (ignoredUrlProperties.getUrls().length > 0) {
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            for (String ignoreUrl : ignoredUrlProperties.getUrls()) {
                // 判断是否匹配
                if (antPathMatcher.match(ignoreUrl, path)) {
                    return true;
                }
            }
        }
        return false;
    }
}
