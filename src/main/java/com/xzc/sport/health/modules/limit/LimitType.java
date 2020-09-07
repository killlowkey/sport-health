package com.xzc.sport.health.modules.limit;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 限流类型
 * 1、方法限流
 * 2、ip限流
 *
 * @author Ray
 * @date created in 2020/9/1 21:46
 */
public enum LimitType {
    METHOD("METHOD"),
    IP("IP");

    @EnumValue
    private final String name;

    LimitType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
