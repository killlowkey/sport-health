package com.xzc.sport.health.controller;

import com.xzc.sport.health.config.GlobalAttributes;
import com.xzc.sport.health.config.GlobalEnum;
import com.xzc.sport.health.domain.ResponseResult;
import com.xzc.sport.health.modules.exception.BadRequestException;
import com.xzc.sport.health.modules.log.Log;
import com.xzc.sport.health.modules.role.Role;
import com.xzc.sport.health.modules.role.UserHolder;
import com.xzc.sport.health.modules.role.hasRoles;
import com.xzc.sport.health.controller.dto.LoginFormDto;
import com.xzc.sport.health.controller.vo.UserVo;
import com.xzc.sport.health.domain.User;
import com.xzc.sport.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author Ray
 * @date created in 2020/8/31 20:55
 */
@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Log("用户登录")
    public ResponseResult login(@RequestBody LoginFormDto loginFormDto, HttpSession session) {
        String username = loginFormDto.getUsername();
        String password = loginFormDto.getPassword();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new BadRequestException(GlobalEnum.USERNAME_OR_PASSWORD_IS_EMPTY);
        }

        User user = userService.findByUsernameAndPassword(username, password);
        if (user == null) {
            throw new BadRequestException(GlobalEnum.USER_NOT_FOUND);
        } else {
            session.setAttribute("user", user);
            return ResponseResult.success(GlobalAttributes.USER_LOGIN_SUCCESS, UserVo.getUserVo(user));
        }

    }


    @GetMapping("/test")
    @hasRoles(Role.ADMIN)
    public String get() {
        return "你好 超级管理员";
    }

    @PostMapping("/logout")
    @Log("用户注销")
    public ResponseResult logout(HttpSession session) {
        User user = UserHolder.getCurrentUser();
        if (user == null) {
            throw new BadRequestException(GlobalEnum.USER_LAYOUT_FAILED);
        } else {
            session.removeAttribute("user");
            return ResponseResult.success(GlobalAttributes.USER_LAYOUT_SUCCESS);
        }
    }

}
