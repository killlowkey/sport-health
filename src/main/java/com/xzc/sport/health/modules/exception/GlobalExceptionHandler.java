package com.xzc.sport.health.modules.exception;

import com.xzc.sport.health.config.GlobalAttributes;
import com.xzc.sport.health.config.GlobalEnum;
import com.xzc.sport.health.domain.ResponseResult;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Ray
 * @date created in 2020/9/1 12:19
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BadRequestException.class, SystemException.class})
    public ResponseResult exceptionHandler(RuntimeException ex) {
        if (ex.getMessage().equalsIgnoreCase(GlobalEnum.USER_NOT_LOGIN.getMessage())) {
            return ResponseResult.error(GlobalAttributes.USER_NOT_LOGIN_CODE, ex.getMessage());
        }
        return ResponseResult.error(ex.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ResponseResult bindHandler(BindException ex) {
        return ResponseResult.success(ex.getMessage());
    }

}
