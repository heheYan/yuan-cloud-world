package com.yuan.cloud.auth.util;

import com.yuan.cloud.auth.client.SystemFeignClient;
import com.yuan.cloud.core.common.constant.YaCommonConst;
import com.yuan.cloud.core.common.constant.YaOauthConst;
import com.yuan.cloud.core.common.constant.YaRedisKeyConst;
import com.yuan.cloud.core.module.system.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Description 关于token信息的工具类
 *
 * @author Mr.Y
 * Created on 2025-08-13 17:20
 */
@Component
public class YaTokenUtil {
    private final JwtDecoder jwtDecoder;
    private final StringRedisTemplate redisTemplate;
    private final SystemFeignClient systemFeignClient;

    public YaTokenUtil(JwtDecoder jwtDecoder, StringRedisTemplate redisTemplate, SystemFeignClient systemFeignClient) {
        this.jwtDecoder = jwtDecoder;
        this.redisTemplate = redisTemplate;
        this.systemFeignClient = systemFeignClient;
    }

    /**
     * Description 缓存token
     *
     * @param token jwt
     */
    public void cacheToken(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        // 设置过期时间, 默认1小时
        long expiresIn = jwt.getExpiresAt() == null ? 3600 : jwt.getExpiresAt().getEpochSecond() - Instant.now().getEpochSecond();
        redisTemplate.opsForValue().set(YaRedisKeyConst.YUAN_LOGIN_TOKEN_CACHE_KEY + jwt.getId(), token,
                expiresIn, TimeUnit.SECONDS);
    }

    /**
     * Description 移除token
     *
     * @param token jwt
     */
    public void removeToken(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        redisTemplate.delete(YaRedisKeyConst.YUAN_LOGIN_TOKEN_CACHE_KEY + jwt.getId());
    }

    /**
     * Description 校验token
     *
     * @param token jwt
     * @return true: 能被正常解析，且在redis中存在
     * false: 不能被正常解析，或者redis中不存在
     */
    public boolean validateToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return redisTemplate.hasKey(YaRedisKeyConst.YUAN_LOGIN_TOKEN_CACHE_KEY + jwt.getId());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Description 获取token中的用户名
     *
     * @param token jwt
     * @return 用户名
     */
    public String getUsername(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject();
    }

    /**
     * Description 获取token中的用户ID
     *
     * @param token jwt
     * @return 用户ID
     */
    public String getUserId(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getClaimAsString(YaOauthConst.JWT_CLAIM_USER_ID);
    }

    /**
     * Description 获取token中的claims信息
     *
     * @param token jwt
     * @return claims信息
     */
    public Map<String, Object> getClaims(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getClaims();
    }

    /**
     * Description 根据token获取用户信息
     *
     * @param token jwt
     * @return 用户信息
     */
    public UserDTO getUser(String token) {
        String username = getUsername(token);
        return systemFeignClient.findByUsername(username).getData();
    }

    /**
     * Description 获取请求头内的token
     *
     * @return 用户信息
     */
    public String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        return request.getHeader(YaOauthConst.AUTH_HEADER_USER);
    }

    /**
     * Description 获取当前登录用户信息
     *
     * @return 用户信息
     */
    public String getCurrentUser() {
        return getUser(getToken()).getUsername();
    }

    /**
     * Description 获取当前请求的TraceId
     *
     * @return TraceId
     */
    public String getCurrentRequestTraceId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        return request.getHeader(YaCommonConst.REQ_HEADER_TRACE_ID);
    }
}
