package com.yuan.cloud.auth.service;

import cn.hutool.core.collection.CollUtil;
import com.yuan.cloud.auth.client.SystemFeignClient;
import com.yuan.cloud.auth.userdetail.YaUserDetails;
import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import com.yuan.cloud.core.module.system.dto.UserDTO;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author YuAN
 * Created on 2025-04-30 16:51
 * @description
 */
@Service
public class YaUserDetailsService implements UserDetailsService {
    @Resource
    SystemFeignClient systemFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = systemFeignClient.findByUsername(username).getData();
        if (userDTO != null) {
            Set<GrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
            if (CollUtil.isNotEmpty(userDTO.getRoles())) {
                userDTO.getRoles().forEach(role -> {
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getCode());
                    simpleGrantedAuthorities.add(simpleGrantedAuthority);
                });
            }
            return YaUserDetails.builder()
                    .username(userDTO.getUsername())
                    .password(userDTO.getPwd())
                    .authorities(simpleGrantedAuthorities)
                    .accountNonExpired(true)
                    .enabled(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build();
        }
        throw new UsernameNotFoundException(YuanStatusEnum.USER_NOT_FOUND.getMsg());
    }
}
