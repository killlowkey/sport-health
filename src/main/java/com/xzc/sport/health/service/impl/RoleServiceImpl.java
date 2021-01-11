package com.xzc.sport.health.service.impl;

import com.xzc.sport.health.mapper.RoleMapper;
import com.xzc.sport.health.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ray
 * @date created in 2020/12/12 10:12
 */
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    @Override
    public Set<GrantedAuthority> getGrantedAuthority(long userId) {
        return roleMapper.getRoleList(userId).stream()
                /**
                 * TODO 这里有坑
                 * 数据要么存储 ROLE_XXX，要么就在这里处理
                 * 如果在 Security 中配置了 antMatch("/xxx").hasRole("ADMIN")
                 * 最终的角色名是 ROLE_ADMIN，如果这里不处理的话那么会无权访问
                 */
                .map(role -> "ROLE_" + role.getName())
                .map(SimpleGrantedAuthority::new)
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }
}
