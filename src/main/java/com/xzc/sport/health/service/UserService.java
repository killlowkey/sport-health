package com.xzc.sport.health.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xzc.sport.health.controller.dto.UserDto;
import com.xzc.sport.health.domain.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Ray
 * @date created in 2020/8/31 21:11
 */
public interface UserService {
    User findByUsernameAndPassword(String username, String password);

    List<User> getAllUser();

    IPage getUserListByPage(IPage<User> page, String queryName);

    int updateUser(UserDto userDto);

    int insertUser(UserDto userDto);

    int deleteUser(long id);

    User findByUsername(String username);
}
