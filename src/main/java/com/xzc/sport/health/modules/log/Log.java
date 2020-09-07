package com.xzc.sport.health.modules.log;

import java.lang.annotation.*;

/**
 * @author Ray
 * @date created in 2020/9/1 13:41
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String value() default "";
}
