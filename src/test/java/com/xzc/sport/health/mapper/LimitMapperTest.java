package com.xzc.sport.health.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Ray
 * @date created in 2020/9/2 13:02
 */
@SpringBootTest
public class LimitMapperTest {
    @Autowired
    private LimitMapper limitMapper;

    @Test
    void findAllLimit() {
        System.out.println(limitMapper.selectList(null));
    }
}
