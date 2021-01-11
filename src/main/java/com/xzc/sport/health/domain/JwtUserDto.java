package com.xzc.sport.health.domain;

import com.xzc.sport.health.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

/**
 * @author Ray
 * @date created in 2020/12/12 9:57
 */
@AllArgsConstructor
public class JwtUserDto implements UserDetails {

    private User user;
    private RoleService roleService;

    @Override
    public Set<? extends GrantedAuthority> getAuthorities() {
        return roleService.getGrantedAuthority(user.getId());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnable();
    }
}
