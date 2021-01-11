package com.xzc.sport.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzc.sport.health.domain.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Ray
 * @date created in 2020/12/12 10:11
 */
public interface RoleMapper extends BaseMapper<Role> {
    @Select("select * from sport_role where user_id=#{userId}")
    List<Role> getRoleList(long userId);
}
