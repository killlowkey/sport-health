package com.xzc.sport.health.service;

import com.xzc.sport.health.domain.Limit;
import com.xzc.sport.health.modules.limit.LimitMatcher;
import com.xzc.sport.health.modules.limit.RequestPathInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Ray
 * @date created in 2021/1/11 10:40
 */
public interface LimitService {
    boolean access(HttpServletRequest request);

    int insertLimit(Limit limit);

    int deleteLimit(long limitId);

    int updateLimit(Limit limit);

    List<Limit> getAllLimit();

    Limit getLimitById(long limitId);

    List<RequestPathInfo> getAllRequestInfo();
}
