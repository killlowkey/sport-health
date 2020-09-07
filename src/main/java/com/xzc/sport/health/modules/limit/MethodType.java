package com.xzc.sport.health.modules.limit;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author Ray
 * @date created in 2020/9/2 8:14
 */
public enum MethodType {
    NONE("NONE"),
    GET("GET"),
    PUT("PUT"),
    POST("POST"),
    DELETE("DELETE");

    @EnumValue
    private final String name;

    MethodType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static MethodType of(String methodName) {
        for (MethodType methodType : values()) {
            if (methodType.name.equalsIgnoreCase(methodName)) {
                return methodType;
            }
        }

        return NONE;
    }
}
