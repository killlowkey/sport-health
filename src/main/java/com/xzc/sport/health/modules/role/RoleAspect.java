package com.xzc.sport.health.modules.role;

import com.xzc.sport.health.config.GlobalEnum;
import com.xzc.sport.health.modules.exception.SystemException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author Ray
 * @date created in 2020/9/1 13:00
 */
@Aspect
@Component
public class RoleAspect {

    @Pointcut("@annotation(com.xzc.sport.health.modules.role.hasRoles)")
    public void point() {}

    @Around("point()")
    public Object roleHandle(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!UserHolder.auth(joinPoint)) {
            throw new SystemException(GlobalEnum.USER_NOT_AUTH);
        }

        return joinPoint.proceed();
    }
}
