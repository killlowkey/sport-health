package com.xzc.sport.health.modules.exception;

import com.xzc.sport.health.config.GlobalEnum;

/**
 * @author Ray
 * @date created in 2020/9/1 12:20
 */
public class BadRequestException extends RuntimeException {
    private int code;

    public BadRequestException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BadRequestException(GlobalEnum globalEnum) {
        this(globalEnum.getCode(), globalEnum.getMessage());
    }

    public BadRequestException(RuntimeException ex) {
        this(500, ex.getMessage());
    }

    public int getCode() {
        return this.code;
    }
}
