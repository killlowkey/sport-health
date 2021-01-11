package com.xzc.sport.health.config;

import lombok.Getter;

/**
 * @author Ray
 * @date created in 2020/9/1 9:57
 */
@Getter
public enum GlobalEnum {

    USER_NOT_LOGIN(1000, "用户未登陆"),
    USERNAME_OR_PASSWORD_IS_EMPTY(1001, "账号或密码为空"),
    USER_NOT_FOUND(1002, "用户不存在"),
    USER_NOT_AUTH(1003, "用户没有权限访问该接口"),
    USER_LAYOUT_FAILED(1004, "用户登出失败"),
    INTERFACE_LIMIT(1005, "已触发接口限流，请稍后重试"),
    USER_STATE_NO_CHANGE(1006, "用户状态不需要改变"),
    USER_DISABLE(1007, "用户已被禁用"),
    USER_INSERT_FAILED(1008, "添加失败，该用户已存在"),
    LIMIT_INTERFACE_NOT_FOUND(1009, "限流接口并未找到"),
    EXCEPTION_LOG_NOT_FOUND(1010, "异常日志未找到"),
    LIMIT_INTERFACE_IS_EXIST(1011, "限流接口已存在"),
    TOKEN_EXPIRED(1012, "Token已过期，请重新进行登录"),
    TOKEN_ERROR(1013, "Token错误"),
    USERNAME_OR_PASSWORD_ERROR(1014, "账号或密码错误");



    private int code;
    private String message;

    GlobalEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
