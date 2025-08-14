package com.yuan.cloud.core.common.security;

import com.yuan.cloud.core.common.util.YaAuthUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author YuAN
 * Created on 2025-05-13 18:35
 * @description
 */
@Slf4j
public class CustomAuthenticationFailureHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            YaAuthUtil.exceptionHandler(request, response, e);
        }
    }
}
