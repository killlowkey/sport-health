package com.xzc.sport.health.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xzc.sport.health.controller.dto.LogQueryDto;
import com.xzc.sport.health.domain.Log;
import org.aspectj.lang.JoinPoint;

/**
 * @author Ray
 * @date created in 2020/9/6 11:11
 */
public interface LogService {
    void save(Log log, JoinPoint joinPoint);

    IPage getUserListByPage(LogQueryDto logQueryDto);

    String getLogException(long id);

    void deleteAllLimit(String logType);
}
