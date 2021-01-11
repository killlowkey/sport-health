package com.xzc.sport.health.mapper;

import com.xzc.sport.health.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void findByUsernameAndPassword() {
        User user = userMapper.findByUsernameAndPassword("admin", "123456");
        System.out.println(user);
    }

    @Test
    void addUser() {
//        userMapper.insert(new User("admin", "123456","2860072080@qq.com", Role.ADMIN));
//        userMapper.insert(new User("user", "123456","976653150@qq.com", Role.USER));

        for (int i = 16; i <= 35; i++) {
//            userMapper.updateById(new User("user" + (i-16), "123456", "2860072080@qq.com", RoleEnum.USER));
        }
    }
}