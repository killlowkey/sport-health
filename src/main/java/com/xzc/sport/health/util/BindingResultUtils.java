package com.xzc.sport.health.util;

import com.xzc.sport.health.modules.exception.BadRequestException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

/**
 * @author Ray
 * @date created in 2020/9/5 12:45
 */
public abstract class BindingResultUtils {
    public static void handler(BindingResult result) throws BindException {
        if (result.hasErrors()) {
            throw new BadRequestException(400, result.getFieldError().getDefaultMessage());
        }
    }
}
