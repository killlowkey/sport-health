package com.xzc.sport.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzc.sport.health.domain.Limit;
import org.apache.ibatis.annotations.Select;

/**
 * @author Ray
 * @date created in 2020/9/2 12:44
 */
public interface LimitMapper extends BaseMapper<Limit> {
    @Select("select * from sport_limit where controller_name=#{controllerName} and method_signature=#{methodSignature}")
    Limit findByControllerAndmethodSignature(String controllerName, String methodSignature);
}
