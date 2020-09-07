package com.xzc.sport.health.modules.limit;

import com.xzc.sport.health.config.GlobalEnum;
import com.xzc.sport.health.domain.Limit;
import com.xzc.sport.health.modules.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Ray
 * @date created in 2020/9/1 21:47
 */
@Aspect
@Component
@Slf4j
public class LimitAspect {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Pointcut("execution(public * com.xzc.sport.health.controller.*Controller.*(..))")
    public void point() {
    }

    @Around("point()")
    public Object limitProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 该接口存在限流接口中
        if (LimitHolder.test(method)) {
            Limit limit = LimitHolder.getLimit(method);
            String key = LimitHolder.getLimitKey(limit);

            // 构建 Lua 脚本
            String luaScript = buildLuaScript();
            RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
            // 获取当前次数
            Number count = redisTemplate.execute(redisScript, Arrays.asList(key), limit.getCount(), limit.getPeriod());

            if (count != null && count.intValue() <= limit.getCount()) {
                log.info("第{}次访问key为 {}，描述为 [{}] 的接口", count, key, limit.getPath());
                return joinPoint.proceed();
            } else {
                throw new BadRequestException(GlobalEnum.INTERFACE_LIMIT);
            }
        }

        return joinPoint.proceed();
    }

    /**
     * 需要使用脚本进行修改数据，不然第一次设置key的过期时间，
     * 然后再去设置key的值，那么过期时间不生效
     *
     * 限流脚本
     */
    private String buildLuaScript() {
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }
}
