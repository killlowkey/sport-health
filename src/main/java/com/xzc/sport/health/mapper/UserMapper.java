package com.xzc.sport.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzc.sport.health.domain.User;
import org.apache.ibatis.annotations.Select;

/**
 * @author Ray
 * @date created in 2020/8/31 21:14
 */
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from sport_user where username=#{username} and password=#{password}")
    User findByUsernameAndPassword(String username, String password);

    @Select("select * from sport_user where username=#{username}")
    User findByUsername(String username);
}
