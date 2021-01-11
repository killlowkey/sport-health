package com.xzc.sport.health.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzc.sport.health.config.GlobalEnum;
import com.xzc.sport.health.config.security.UserHolder;
import com.xzc.sport.health.controller.dto.LogQueryDto;
import com.xzc.sport.health.domain.Log;
import com.xzc.sport.health.mapper.LogMapper;
import com.xzc.sport.health.modules.exception.BadRequestException;
import com.xzc.sport.health.service.LogService;
import com.xzc.sport.health.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ray
 * @date created in 2020/9/6 11:12
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public void save(Log log, JoinPoint joinPoint) {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取方法上的 Log 注解
        com.xzc.sport.health.modules.log.Log logAnnotation = AnnotationUtils.findAnnotation(method,
                com.xzc.sport.health.modules.log.Log.class);
        // 设置日志值
        log.setDescription(logAnnotation.value());

        // 设置方法名
        log.setMethod(StringUtils.getSimpleMethodDesc(method));

        // 设置方法参数
        log.setParams(StringUtils.getMethodParams(joinPoint));

        // 设置操作用户
        if (log.getUsername().equalsIgnoreCase("未知用户")) {
            log.setUsername(UserHolder.getUsername());
        }

        // 设置请求ip
        log.setRequestIp(StringUtils.getRequestIp());

        // 设置ip地址
        log.setAddress(StringUtils.getCurrentIpAddress());

        // 设置浏览器
        log.setBrowser(StringUtils.getCurrentBrowser());

        logMapper.insert(log);
    }

    @Override
    public IPage getUserListByPage(LogQueryDto logQueryDto) {
        Page<Log> page = new Page<>(logQueryDto.getPageNum(), logQueryDto.getPageSize());
        QueryWrapper<Log> queryWrapper = new QueryWrapper<>();

        String descName = logQueryDto.getDescName();
        if (StringUtils.hasText(descName)) {
            queryWrapper.like("description", descName);
        }

        String logType = logQueryDto.getLogType();
        if (StringUtils.hasText(logType) &&
                (logType.equalsIgnoreCase("INFO") || logType.equalsIgnoreCase("ERROR"))) {
            queryWrapper.eq("log_type", logType);
        }

        List<LocalDateTime> times = logQueryDto.getTimes();
        if (times != null && times.size() == 2) {
            queryWrapper.between("create_time", times.get(0), times.get(1));
        }

        String idSort = logQueryDto.getIdSort();
        if (StringUtils.hasText(idSort) && idSort.equalsIgnoreCase("desc")) {
            queryWrapper.orderByDesc("id");
        }
        return logMapper.selectPage(page, queryWrapper);
    }

    @Override
    public String getLogException(long id) {
        Log log = logMapper.findExceptionById(id);
        if (log == null) {
            throw new BadRequestException(GlobalEnum.EXCEPTION_LOG_NOT_FOUND);
        }

        return new String(log.getExceptionDetail());
    }

    @Override
    public void deleteAllLimit(String logType) {
        List<Long> idList = new ArrayList<>();
        logMapper.findLogByLogType(logType).forEach(log -> idList.add(log.getId()));
        logMapper.deleteBatchIds(idList);
    }
}
