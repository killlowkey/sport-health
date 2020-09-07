package com.xzc.sport.health.modules.log;

import com.xzc.sport.health.domain.Log;
import com.xzc.sport.health.modules.role.UserHolder;
import com.xzc.sport.health.service.LogService;
import com.xzc.sport.health.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import com.xzc.sport.health.util.ThrowableUtils;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 日志切面，记录接口的执行时间，方法参数、执行人等等
 *
 * @author Ray
 * @date created in 2020/9/1 18:15
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogService logService;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    @Pointcut("@annotation(com.xzc.sport.health.modules.log.Log)")
    public void point() {
    }

    @Around("point()")
    public Object logProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime.set(System.currentTimeMillis());
        // 如果调用的是注销方法，那么执行方法之后就获取不到用户名了，所以现在得提前获取用户名
        String username = UserHolder.getUsername();
        result = joinPoint.proceed();

        Log log = new Log("INFO", username, System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        logService.save(log, joinPoint);

        return result;
    }

    @AfterThrowing(value = "point()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Log log = new Log("ERROR", UserHolder.getUsername(), System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        log.setExceptionDetail(ThrowableUtils.getStackTrace(e).getBytes());

        logService.save(log, joinPoint);
    }
}
