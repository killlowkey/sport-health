package com.xzc.sport.health.service.impl;

import com.xzc.sport.health.config.GlobalEnum;
import com.xzc.sport.health.domain.Limit;
import com.xzc.sport.health.mapper.LimitMapper;
import com.xzc.sport.health.modules.exception.BadRequestException;
import com.xzc.sport.health.modules.limit.LimitMatcher;
import com.xzc.sport.health.modules.limit.RequestPathInfo;
import com.xzc.sport.health.service.LimitService;
import com.xzc.sport.health.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ray
 * @date created in 2021/1/11 10:42
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LimitServiceImpl implements LimitService, CommandLineRunner {

    private final LimitMapper limitMapper;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final List<LimitMatcher> limitMatcherList = new ArrayList<>();
    private final List<RequestPathInfo> pathInfoList = new ArrayList<>();
    private final RequestMappingHandlerMapping handlerMapping;
    private final WebMvcProperties webMvcProperties;

    @Override
    public boolean access(HttpServletRequest request) {
        LimitMatcher matcher = getMatcher(request);
        if (matcher != null) {
            String limitKey = matcher.generateKey(request);
            Limit limit = matcher.getLimit();
            // 构建 Lua 脚本
            RedisScript<Number> redisScript = new DefaultRedisScript<>(buildLuaScript(), Number.class);
            // 获取当前次数
            Number count = redisTemplate.execute(redisScript, Collections.singletonList(limitKey),
                    limit.getCount(),
                    limit.getPeriod());

            log.info("第{}次访问key为 {}，描述为 [{}] 的接口", count, limitKey, limit.getPath());
            return count != null && count.intValue() <= limit.getCount();
        }

        return true;
    }

    @Override
    public int insertLimit(Limit limit) {
        // 插入到数据库
        int result = limitMapper.insert(limit);
        // 插入到本地限流
        limitMatcherList.add(new LimitMatcher(limit));
        return result;
    }

    @Override
    public int deleteLimit(long limitId) {
        Limit limit = getLimitById(limitId);
        // 数据库删除 limit
        int result = limitMapper.deleteById(limitId);
        // 本地限流中删除 LimitMatcher 就ok了，不用从redis删除，让redis等待过期就好了
        limitMatcherList.remove(getMatcher(limit));
        return result;
    }

    @Override
    public int updateLimit(Limit limit) {
        Limit resultLimit = limitMapper.selectById(limit.getId());
        if (resultLimit == null) {
            throw new BadRequestException(GlobalEnum.LIMIT_INTERFACE_NOT_FOUND);
        }

        int result = limitMapper.updateById(limit);
        // 修改本地的 LimitMatcher
        LimitMatcher matcher = getMatcher(limit);
        if (matcher != null) {
            matcher.setLimit(limit);
        }
        return result;
    }

    @Override
    public List<Limit> getAllLimit() {
        return limitMapper.selectList(null);
    }

    @Override
    public Limit getLimitById(long limitId) {
        Limit limit = limitMapper.selectById(limitId);
        if (limit == null) {
            throw new BadRequestException(GlobalEnum.LIMIT_INTERFACE_NOT_FOUND);
        }
        return limit;
    }

    @Override
    public List<RequestPathInfo> getAllRequestInfo() {
        return this.pathInfoList;
    }

    private LimitMatcher getMatcher(HttpServletRequest request) {
        for (LimitMatcher matcher : limitMatcherList) {
            if (matcher.match(request)) {
                return matcher;
            }
        }

        return null;
    }

    private LimitMatcher getMatcher(Limit limit) {
        LimitMatcher limitMatcher = new LimitMatcher(limit.getPath(), limit.getMethod());
        for (LimitMatcher matcher : limitMatcherList) {
            if (matcher.equals(limitMatcher)) {
                return matcher;
            }
        }

        return null;
    }

    /**
     * 需要使用脚本进行修改数据，不然第一次设置key的过期时间，
     * 然后再去设置key的值，那么过期时间不生效
     * <p>
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

    @Override
    public void run(String... args) throws Exception {
        // 从数据库中加载 limit
        log.info("正在加载限流接口......");
        limitMapper.selectList(null).forEach(limit -> limitMatcherList.add(new LimitMatcher(limit)));

        // 加载所有的 RequestMapping
        String path = webMvcProperties.getServlet().getPath();
        handlerMapping.getHandlerMethods()
                .forEach((requestMappingInfo, handlerMethod) -> {
                    // {POST /authorize/logout} ==> POST /authorize/logout
                    String pathInfo = requestMappingInfo.toString();
                    String info = pathInfo.substring(1, pathInfo.length() - 1);
                    String[] s = info.split(" ");
                    if (StringUtils.hasText(s[0])) {
                        pathInfoList.add(new RequestPathInfo(HttpMethod.valueOf(s[0]), path + s[1]));
                    }
                });
    }
}
