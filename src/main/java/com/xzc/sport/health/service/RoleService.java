package com.xzc.sport.health.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * @author Ray
 * @date created in 2020/12/12 10:11
 */
public interface RoleService {
    /**
     * 根据用户id获取对应的用户角色
     *
     * @param userId 用户id
     * @return
     */
    Set<GrantedAuthority> getGrantedAuthority(long userId);
}
