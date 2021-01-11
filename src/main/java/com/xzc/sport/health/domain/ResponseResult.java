package com.xzc.sport.health.domain;

import com.xzc.sport.health.config.GlobalEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Ray
 * @date created in 2020/9/1 9:51
 */
@Data
@AllArgsConstructor
public class ResponseResult {

    private static final int ERROR_CODE = 400;
    private static final int SUCCESS_CODE  = 200;

    private int code;
    private String message;
    private Object data;

    public static ResponseResult success() {
        return success("success", null);
    }

    public static ResponseResult success(Object data) {
        return success("success", data);
    }

    public static ResponseResult success(String message) {
        return success(message, null);
    }

    public static ResponseResult success(GlobalEnum globalEnum) {
        return success(globalEnum.getMessage());
    }

    public static ResponseResult success(String message, Object data) {
        return new ResponseResult(SUCCESS_CODE, message, data);
    }


    public static ResponseResult error() {
        return error("");
    }

    public static ResponseResult error(String message)  {
        return error(ERROR_CODE, message);
    }

    public static ResponseResult error(int code, String message) {
        return new ResponseResult(code, message, null);
    }

    public static ResponseResult error(GlobalEnum globalEnum) {
        return error(globalEnum.getMessage());
    }
}
