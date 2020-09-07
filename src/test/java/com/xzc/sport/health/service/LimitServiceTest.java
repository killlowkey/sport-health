package com.xzc.sport.health.service;

import com.xzc.sport.health.domain.Limit;
import com.xzc.sport.health.mapper.LimitMapper;
import com.xzc.sport.health.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LimitServiceTest {

    @Autowired
    LimitMapper limitMapper;

    @Test
    void update() {
        Limit limit = limitMapper.selectById(1);
        limit.setState(false);
        limitMapper.updateById(limit);

        System.out.println(limitMapper.selectById(1));
    }
}