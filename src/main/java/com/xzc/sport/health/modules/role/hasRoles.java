package com.xzc.sport.health.modules.role;

import java.lang.annotation.*;

/**
 * 角色注解，拥有某种角色的用户就能访问接口
 *
 * @author Ray
 * @date created in 2020/9/1 12:57
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface hasRoles {
    Role[] value();
}
