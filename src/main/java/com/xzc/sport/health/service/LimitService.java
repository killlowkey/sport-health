package com.xzc.sport.health.service;

import com.xzc.sport.health.domain.Limit;

import java.util.List;

/**
 * @author Ray
 * @date created in 2020/9/2 12:44
 */
public interface LimitService {
    int update(Limit limit);
    List<Limit> findAllLimit();
    int deleteLimitById(long id);
    int insertLimit(Limit limit);
}
