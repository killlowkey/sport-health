package com.xzc.sport.health.modules.exception;

import com.xzc.sport.health.config.GlobalEnum;

/**
 * 系统异常
 *
 * @author Ray
 * @date created in 2020/9/1 13:29
 */
public class SystemException extends RuntimeException {
    private int code;

    public SystemException(GlobalEnum globalEnum) {
        this(globalEnum.getCode(), globalEnum.getMessage());
    }

    public SystemException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
