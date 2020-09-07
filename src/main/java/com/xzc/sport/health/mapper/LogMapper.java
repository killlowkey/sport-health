package com.xzc.sport.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzc.sport.health.domain.Log;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Ray
 * @date created in 2020/9/6 11:13
 */
public interface LogMapper extends BaseMapper<Log> {
    @Select("select * from sport_log where log_type=\"ERROR\" and id=#{id}")
    Log findExceptionById(long id);


    @Select("select * from sport_log where log_type=#{logType}")
    List<Log> findLogByLogType(String logType);
}
