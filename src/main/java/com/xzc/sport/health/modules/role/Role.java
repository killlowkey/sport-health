package com.xzc.sport.health.modules.role;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author Ray
 * @date created in 2020/9/1 12:57
 */
public enum Role {
    ADMIN("ADMIN", "超级管理员"),
    USER("USER", "普通用户");

    @EnumValue
    private final String name;
    private final String desc;

    Role(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return this.name;
    }
}
