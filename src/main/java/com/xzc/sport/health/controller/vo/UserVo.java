package com.xzc.sport.health.controller.vo;

import com.xzc.sport.health.domain.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author Ray
 * @date created in 2020/9/1 21:49
 */
@Data
public class UserVo {
    private String username;
    private String email;
//    private RoleEnum role;

    public static UserVo getUserVo(User user) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }
}
