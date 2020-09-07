package com.xzc.sport.health.controller.dto;

import com.xzc.sport.health.modules.role.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Ray
 * @date created in 2020/9/4 13:54
 */
@Data
public class UserDto {
    private long id;

    @NotNull(message = "用户名不能为空")
    @Size(min = 5, max = 12, message = "用户名需在5-12字符之间")
    private String username;

    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 10, message = "用户名需在5-10字符之间")
    private String password;

    @NotNull(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;

    private Role role;

    private boolean state;
}
