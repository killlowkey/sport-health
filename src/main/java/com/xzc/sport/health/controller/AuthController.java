package com.xzc.sport.health.controller;

import com.xzc.sport.health.config.GlobalAttributes;
import com.xzc.sport.health.domain.ResponseResult;
import com.xzc.sport.health.modules.log.Log;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Ray
 * @date created in 2020/8/31 20:55
 */
@RestController
@RequestMapping("/authorize")
public class AuthController {

    @PostMapping("/logout")
    @Log("用户注销")
    public ResponseResult logout() {
        // 删除认证信息
        SecurityContextHolder.getContext().setAuthentication(null);

        return ResponseResult.success(GlobalAttributes.USER_LOGOUT_SUCCESS);
    }

}
