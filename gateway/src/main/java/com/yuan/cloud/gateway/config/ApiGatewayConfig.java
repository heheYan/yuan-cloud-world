package com.yuan.cloud.gateway.config;

import com.alibaba.fastjson2.JSONObject;
import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import com.yuan.cloud.core.common.property.IgnoredUrlProperties;
import com.yuan.cloud.core.common.response.YuanR;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Description 网关配置
 *
 * @author Mr.Y
 * Created on 2025-08-11 16:41
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class ApiGatewayConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    final IgnoredUrlProperties ignoredUrlProperties;

    /**
     * 认证失败处理
     *
     * @param exchange  交换
     * @param exception 异常
     * @return Mono
     */
    public static Mono<Void> accessDeniedHandler(ServerWebExchange exchange, RuntimeException exception) {
        log.error("认证失败:{}", exception.getMessage());
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        YuanR<String> fail = YuanR.fail(YuanStatusEnum.UNAUTHORIZED);
        fail.setData(exception.getMessage());
        byte[] bits = JSONObject.from(fail).toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 认证失败处理
     *
     * @param exchange  交换
     * @param exception 认证异常
     * @return Mono
     */
    public static Mono<Void> authenticationFailureHandler(WebFilterExchange exchange, AuthenticationException exception) {
        return accessDeniedHandler(exchange.getExchange(), exception);
    }

    @Bean
    public SecurityWebFilterChain defaultSecurityFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        // 放行配置白名单的接口
                        .pathMatchers(ignoredUrlProperties.getUrls()).permitAll()
                        .pathMatchers("/api/admin/user/test").hasRole("ADMIN")
                        // 拦截其他接口需要登录
                        .anyExchange().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.
                        jwt(Customizer.withDefaults())
                        .accessDeniedHandler(ApiGatewayConfig::accessDeniedHandler) // 认证失败处理
                        .authenticationEntryPoint(ApiGatewayConfig::accessDeniedHandler) // 认证失败处理
                        .authenticationFailureHandler(ApiGatewayConfig::authenticationFailureHandler));
        return httpSecurity.build();
    }

    @Bean
    @Lazy // 懒加载，保证网关不受认证服务影响
    public JwtDecoder jwtDecoder() {
        // 使用与auth-service相同的JWK端点
        return NimbusJwtDecoder.withJwkSetUri(issuerUri + "/oauth2/jwks")
                .build();
    }
}
