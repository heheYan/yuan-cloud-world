package com.yuan.cloud.core.config.security;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.StrUtil;
import com.yuan.cloud.core.common.constant.YaCommonConst;
import com.yuan.cloud.core.common.constant.YaOauthConst;
import com.yuan.cloud.core.common.constant.YaRedisKeyConst;
import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import com.yuan.cloud.core.common.exception.YuanApiException;
import com.yuan.cloud.core.common.property.IgnoredUrlProperties;
import com.yuan.cloud.core.common.security.CustomAuthenticationFailureHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Description 项目通用的安全配置
 *
 * @author Mr.Y
 * Created on 2025-08-12 14:29
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty( // 可通过配置控制是否启用
        name = "yuan.config.scan.enabled",
        havingValue = "true",
        matchIfMissing = true // 默认启用
)
@RequiredArgsConstructor
public class YaBaseSecurityConfig {

    @Schema(description = "白名单接口")
    private final IgnoredUrlProperties ignoredUrlProperties;

    private final StringRedisTemplate redisTemplate;

    /**
     * 默认的安全策略
     *
     * @param http security注入点
     * @return SecurityFilterChain
     * @throws Exception 抛出异常
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // 忽略跨域请求
                .cors(AbstractHttpConfigurer::disable)
                // 忽略csrf
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用session
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // 设置自定义客户端权限验证
                        .anyRequest().access(customClientAuthorizationManager()));
        http.addFilterBefore(new CustomAuthenticationFailureHandler(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * 自定义客户端权限验证
     *
     * @return AuthorizationManager 客户端权限验证
     */
    private AuthorizationManager<RequestAuthorizationContext> customClientAuthorizationManager() {
        return (authentication, context) -> {
            HttpServletRequest request = context.getRequest();
            // 只放行从网关传递过来的请求
            String yaTraceId = request.getHeader(YaCommonConst.REQ_HEADER_TRACE_ID);
            // 如果客户端请求头中不存在客户端token，则拒绝访问
            if (StrUtil.isEmptyIfStr(yaTraceId)) {
                throw new YuanApiException(YuanStatusEnum.TOKEN_NOT_EXIST);
            }
            // 判断redis内是否存在客户端token
            if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(YaRedisKeyConst.SERVER_HEADER_TRACE_ID, yaTraceId))) {
                // 1. 判断是否白名单接口，如果不是，需要再判断请求头是否包含网关解析后添加的用户信息
                if (!isExcludePath(request.getRequestURI())) {
                    if (StrUtil.isEmptyIfStr(request.getHeader(YaOauthConst.AUTH_HEADER_USER))) {
                        throw new YuanApiException(YuanStatusEnum.TOKEN_INVALID);
                    }
                }
                // token 认证通过
                return new AuthorizationDecision(true);
            } else {
                // 不存在，标识该token已使用过，拒绝访问
                throw new YuanApiException(YuanStatusEnum.TOKEN_EXPIRED);
            }
        };
    }

    @Operation(description = "判断是否是白名单接口")
    private boolean isExcludePath(String path) {
        // 允许匿名访问的接口无需判断
        if (path.contains("anonymous")) {
            return true;
        }
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
