package com.xzc.sport.health.service.impl;

import com.xzc.sport.health.config.GlobalAttributes;
import com.xzc.sport.health.domain.JwtUserDto;
import com.xzc.sport.health.domain.User;
import com.xzc.sport.health.service.RoleService;
import com.xzc.sport.health.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Ray
 * @date created in 2020/12/12 10:19
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    private final RoleService roleService;

    @Override
    public JwtUserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(GlobalAttributes.USER_NOT_FOUND);
        }

        return new JwtUserDto(user, roleService);
    }
}
