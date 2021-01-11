package com.xzc.sport.health.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xzc.sport.health.config.GlobalEnum;
import com.xzc.sport.health.controller.dto.UserDto;
import com.xzc.sport.health.domain.User;
import com.xzc.sport.health.mapper.UserMapper;
import com.xzc.sport.health.modules.exception.BadRequestException;
import com.xzc.sport.health.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ray
 * @date created in 2020/8/31 21:13
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user = userMapper.findByUsernameAndPassword(username, password);
        // user#state == false 说明该用户被禁用了
        if (user != null && !user.isEnable()) {
            throw new BadRequestException(GlobalEnum.USER_DISABLE);
        }
        return user;
    }

    @Override
    public List<User> getAllUser() {
        return userMapper.selectList(null);
    }

    @Override
    public int updateUser(UserDto userDto) {
        User user = userMapper.selectById(userDto.getId());
        if (user == null) {
            throw new BadRequestException(GlobalEnum.USER_NOT_FOUND);
        }

        BeanUtils.copyProperties(userDto, user);
        return userMapper.updateById(user);
    }

    @Override
    public int insertUser(UserDto userDto) {
        User user = userMapper.findByUsername(userDto.getUsername());
        if (user != null) {
            throw new BadRequestException(GlobalEnum.USER_INSERT_FAILED);
        }

        user = new User();
        BeanUtils.copyProperties(userDto, user);
        return userMapper.insert(user);
    }

    @Override
    public int deleteUser(long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BadRequestException(GlobalEnum.USER_NOT_FOUND);
        }

        return userMapper.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public IPage getUserListByPage(IPage page, String queryName) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("username", queryName);
        return userMapper.selectPage(page, wrapper);
    }
}
