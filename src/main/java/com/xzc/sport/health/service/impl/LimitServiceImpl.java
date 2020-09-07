package com.xzc.sport.health.service.impl;

import com.xzc.sport.health.config.GlobalEnum;
import com.xzc.sport.health.domain.Limit;
import com.xzc.sport.health.mapper.LimitMapper;
import com.xzc.sport.health.modules.exception.BadRequestException;
import com.xzc.sport.health.modules.limit.LimitHolder;
import com.xzc.sport.health.modules.limit.LimitType;
import com.xzc.sport.health.service.LimitService;
import com.xzc.sport.health.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ray
 * @date created in 2020/9/2 12:45
 */
@Service
public class LimitServiceImpl implements LimitService {

    @Autowired
    private LimitMapper limitMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public List<Limit> findAllLimit() {
        return limitMapper.selectList(null);
    }

    @Override
    public int deleteLimitById(long id) {
        Limit limit = limitMapper.selectById(id);
        if (limit == null) {
            throw new BadRequestException(GlobalEnum.LIMIT_INTERFACE_NOT_FOUND);
        }

        LimitHolder.removeLimit(limit);
        return limitMapper.deleteById(id);
    }

    @Override
    public int insertLimit(Limit limit) {
        LimitHolder.getPathInfoMap()
                .getOrDefault(limit.getControllerName(), new ArrayList<>())
                .forEach(pathInfo -> {
                    if (pathInfo.getPath().equalsIgnoreCase(limit.getPath()) &&
                            pathInfo.getMethodType().equals(limit.getMethodType())) {
                        limit.setMethodSignature(pathInfo.getMethodSignature());
                    }
                });

        if (!StringUtils.hasText(limit.getMethodSignature())) {
            limit.setMethodSignature("unknown");
        }

        Limit tempLimit =
                limitMapper.findByControllerAndmethodSignature(limit.getControllerName(), limit.getMethodSignature());
        if (tempLimit != null) {
            throw new BadRequestException(GlobalEnum.LIMIT_INTERFACE_IS_EXIST);
        }

        int line = limitMapper.insert(limit);
        LimitHolder.addLimit(limit);
        return line;
    }

    @Override
    public int update(Limit limit) {
        Limit tempLimit = limitMapper.selectById(limit.getId());
        if (tempLimit == null) {
            throw new BadRequestException(GlobalEnum.LIMIT_INTERFACE_NOT_FOUND);
        }

        int line = limitMapper.updateById(limit);
        if (line > 0) {
            String key = LimitHolder.getLimitKey(tempLimit);
            redisTemplate.delete(key);
            LimitHolder.removeLimit(tempLimit);
            LimitHolder.addLimit(limit);
        }

        return line;
    }
}
